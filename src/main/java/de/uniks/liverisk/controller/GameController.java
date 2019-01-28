package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.*;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class GameController {

    private static final ArrayList<String> DEFAULT_NAMES = new ArrayList<>(Arrays.asList("Arthur", "Bill", "Charles", "Dutch"));
    private static final ArrayList<String> DEFAULT_COLORS = new ArrayList<>(Arrays.asList("0x336633ff", "0xe64d4dff", "0x4d66ccff", "0xcccc33ff"));

    private static final int PLATFORM_COUNT_MODIFIER = 3;
    private static final ArrayList<Integer> PLATFORM_CAPACITIES = new ArrayList<>(Arrays.asList(3, 4, 5));

    private static final int PLATFORM_SPACING = 50;

    private static final int LOWER_X_POS_BOUND = 50;
    private static final int LOWER_Y_POS_BOUND = 50;
    private static final int UPPER_X_POS_BOUND = GameScreenController.GAME_SCREEN_HEIGHT - PlatformController.PLATFORM_WIDTH - 50;
    private static final int UPPER_Y_POS_BOUND = GameScreenController.GAME_SCREEN_HEIGHT - PlatformController.PLATFORM_HEIGHT - 50;

    public static final int MAX_SPARE_UNIT_COUNT = 16;

    private static GameController instance;

    public static GameController getInstance() {
        if (instance == null) instance = new GameController();
        return instance;
    }

    public static void clear() {
        instance = null;
    }

    private GameLoop gameLoop;

    private Semaphore mutex = new Semaphore(1);

    private GameController() {
        //singleton
    }

    public void initGame(final int playerCount, final int nonPlayerCharacterCount) {
        initGame(playerCount);
        addNonPlayerCharactersToGameLoop(nonPlayerCharacterCount);
    }

    public void initGame(final int playerCount) {
        if (playerCount < 2 || playerCount > 4) return;

        initPlayers(playerCount);

        initPlatforms(playerCount * PLATFORM_COUNT_MODIFIER);
    }

    private void initPlayers(final int playerCount) {
        Game game = Model.getInstance().getGame();
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player();
            player.setName(DEFAULT_NAMES.get(i))
                    .setColor(DEFAULT_COLORS.get(i))
                    .withUnits(new Unit(), new Unit(), new Unit(), new Unit());
            game.withPlayers(player);
        }
        //should not be changed if playing versus the computer
        //only platforms owned by the current player can be selected as the current platform or reenforced through ui inputs
        game.setCurrentPlayer(game.getPlayers().get(0));
    }

    private void initPlatforms(final int platformCount) {
        Game game = Model.getInstance().getGame();
        for (int i = 0; i < platformCount; i++) {
            game.withPlatforms(new Platform());
        }

        randomizePlatformLocations();
        randomizePlatformConnections();
        setStartingPlatforms();
        setPlatformCapacties();
    }

    private void randomizePlatformLocations() {
        for (Platform platform : Model.getInstance().getGame().getPlatforms()) {
            do {
                int xPos = ThreadLocalRandom.current().nextInt(LOWER_X_POS_BOUND, UPPER_X_POS_BOUND);
                int yPos = ThreadLocalRandom.current().nextInt(LOWER_Y_POS_BOUND, UPPER_Y_POS_BOUND);
                platform.setXPos(xPos)
                        .setYPos(yPos);
            } while (!platformPlacementIsValid(platform));
        }
    }

    //TODO ensure that there are not multiple sub graphs
    private void randomizePlatformConnections() {
        for (Platform platform : Model.getInstance().getGame().getPlatforms()) {
            List<Platform> closeNeighors = getClosePlatforms(platform, 2);
            platform.withNeighbors(closeNeighors);
        }
    }

    private List<Platform> getClosePlatforms(final Platform platform, int count) {
        ArrayList<Platform> platforms = new ArrayList<>(Model.getInstance().getGame().getPlatforms());
        platforms.remove(platform);
        //platforms.remove(platform.getNeighbors());
        platforms.sort(Comparator.comparingInt(p -> (int) getDistance(p, platform)));
        return platforms.subList(0, count);
    }

    private double getDistance(final Platform a, final Platform b) {
        double xComponent = b.getXPos() - a.getXPos();
        double yComponent = b.getYPos() - a.getYPos();
        return Math.sqrt(Math.pow(xComponent, 2) + Math.pow(yComponent, 2));
    }

    private void setStartingPlatforms() {
        for (Player player : Model.getInstance().getGame().getPlayers()) {
            Platform startingPlatform = getPossibleStartingPlatform();
            startingPlatform.setPlayer(player)
                    .withUnits(new Unit());
        }
    }

    private Platform getPossibleStartingPlatform() {
        ArrayList<Platform> platforms = new ArrayList<>(Model.getInstance().getGame().getPlatforms());
        Collections.shuffle(platforms);
        return platforms.stream()
                .filter(platform -> platform.getPlayer() == null
                        && platform.getNeighbors().stream()
                        .noneMatch(neighbor -> neighbor.getPlayer() != null))
                .findAny().orElse(null);
    }

    private void setPlatformCapacties() {
        for (Platform platform : Model.getInstance().getGame().getPlatforms()) {
            if (platform.getPlayer() != null) {
                platform.setCapacity(PLATFORM_CAPACITIES.get(PLATFORM_CAPACITIES.size() - 1));
            } else {
                int platformCapacity = PLATFORM_CAPACITIES.get(ThreadLocalRandom.current().nextInt(PLATFORM_CAPACITIES.size()));
                platform.setCapacity(platformCapacity);
            }
        }
    }

    private boolean platformPlacementIsValid(final Platform platform) {
        return Model.getInstance().getGame().getPlatforms()
                .parallelStream()
                .noneMatch(p -> p.getXPos() != 0 && !p.equals(platform) && platformsOverlap(platform, p));
    }

    private boolean platformsOverlap(final Platform a, final Platform b) {
        if (a.getYPos() + PlatformController.PLATFORM_HEIGHT + PLATFORM_SPACING < b.getYPos()
                || a.getYPos() > b.getYPos() + PlatformController.PLATFORM_HEIGHT + PLATFORM_SPACING) {
            return false;
        }
        if (a.getXPos() + PlatformController.PLATFORM_WIDTH + PLATFORM_SPACING < b.getXPos()
                || a.getXPos() > b.getXPos() + PlatformController.PLATFORM_WIDTH + PLATFORM_SPACING) {
            return false;
        }
        return true;
    }

    private void addNonPlayerCharactersToGameLoop(final int nonPlayerCharacterCount) {
        final ArrayList<Player> players = Model.getInstance().getGame().getPlayers();
        final ArrayList<NonPlayerCharacter> nonPlayerCharacters = new ArrayList<>();

        if (nonPlayerCharacterCount >= players.size()) Model.getInstance().getGame().setCurrentPlayer(null);

        for (int i = players.size() - nonPlayerCharacterCount; i < players.size(); i++) {
            nonPlayerCharacters.add(new NonPlayerCharacter(players.get(i)));
        }

        gameLoop = new GameLoop(nonPlayerCharacters);
    }

    public void startGameLoop() {
        if (gameLoop == null) gameLoop = new GameLoop();
        gameLoop.start();
    }

    public void stopGameLoop() {
        if (gameLoop != null) gameLoop.stop();
    }

    public boolean concurrentMove(final Platform source, final Platform destination) {
        boolean moved = false;
        try {
            try {
                mutex.acquire();
                moved = move(source, destination);
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return moved;
    }

    private boolean move(final Platform source, final Platform destination) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(destination);

        if (!moveIsPossible(source, destination)) return false;

        int emptyDestinationSlots = destination.getCapacity() - destination.getUnits().size();
        int unitsToMoveCount = Math.min(emptyDestinationSlots, source.getUnits().size() - 1);

        ArrayList<Unit> unitsToMove = new ArrayList<>(source.getUnits());
        unitsToMove.stream()
                .limit(unitsToMoveCount)
                .forEach(unit -> unit.setPlatform(destination));

        destination.setPlayer(source.getPlayer());
        return true;
    }

    private boolean moveIsPossible(final Platform source, final Platform destination) {
        return source.getPlayer() != null &&
                (destination.getPlayer() == null || source.getPlayer() == destination.getPlayer()) &&
                source.getNeighbors().contains(destination) &&
                destination.getUnits().size() < destination.getCapacity() &&
                source.getUnits().size() > 1;
    }

    public boolean concurrentAttack(final Platform source, final Platform destination) {
        boolean attacked = false;
        try {
            try {
                mutex.acquire();
                attacked = attack(source, destination);
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return attacked;
    }

    private boolean attack(final Platform source, final Platform destination) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(destination);

        if (!attackIsPossible(source, destination)) return false;

        int lostUnitCount = Math.min(source.getUnits().size() - 1, destination.getUnits().size());

        ArrayList<Unit> attackers = new ArrayList<>(source.getUnits());
        ArrayList<Unit> defenders = new ArrayList<>(destination.getUnits());
        attackers.stream()
                .limit(lostUnitCount)
                .forEach(Unit::removeYou);
        defenders.stream()
                .limit(lostUnitCount)
                .forEach(Unit::removeYou);

        if (destination.getUnits().isEmpty()) {
            destination.setPlayer(null);
            move(source, destination);
        }
        return true;
    }

    private boolean attackIsPossible(final Platform source, final Platform destination) {
        return source.getPlayer() != null &&
                destination.getPlayer() != null &&
                source.getPlayer() != destination.getPlayer() &&
                source.getNeighbors().contains(destination) &&
                source.getUnits().size() > 1;
    }

    public boolean reenforce(final Platform platform) {
        Objects.requireNonNull(platform);

        if (!reenforcementIsPossible(platform)) return false;

        int emptySlots = platform.getCapacity() - platform.getUnits().size();

        ArrayList<Unit> spareUnits = new ArrayList<>(platform.getPlayer().getUnits());
        spareUnits.stream()
                .limit(emptySlots)
                .forEach(unit -> {
                    unit.setPlatform(platform)
                            .setPlayer(null);
                });

        return true;
    }

    private boolean reenforcementIsPossible(final Platform platform) {
        return platform.getPlayer() != null &&
                platform.getUnits().size() < platform.getCapacity() &&
                !platform.getPlayer().getUnits().isEmpty();
    }

    //checks if player names or colors include duplicates
    boolean playerConfigurationIsValid() {
        Game game = Model.getInstance().getGame();
        HashSet<String> nameSet = new HashSet<>();
        HashSet<String> colorSet = new HashSet<>();
        game.getPlayers().stream()
                .filter(player -> !player.getName().isEmpty() && !player.getColor().isEmpty())
                .forEach(player -> {
                    nameSet.add(player.getName());
                    colorSet.add(player.getColor());
                });
        return nameSet.size() == game.getPlayers().size() && colorSet.size() == game.getPlayers().size();
    }


}
