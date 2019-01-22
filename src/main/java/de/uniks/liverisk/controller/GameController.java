package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.*;

import java.util.*;

public class GameController {

    private static final ArrayList<String> DEFAULT_NAMES = new ArrayList<>(Arrays.asList("Arthur", "Bill", "Charles", "Dutch"));
    private static final ArrayList<String> DEFAULT_COLORS = new ArrayList<>(Arrays.asList("0x336633ff", "0xe64d4dff", "0x4d66ccff", "0xcccc33ff"));

    private static final int STARTING_PLATFORM_CAPACITY = 5;
    private static final int REGULAR_PLATFORM_CAPACITY = 3;

    private static GameController instance;

    public static GameController getInstance() {
        if (instance == null) instance = new GameController();
        return instance;
    }

    public static void clear() {
        instance = null;
    }

    private GameLoop gameLoop;

    private GameController() {
        //singleton
    }

    //TODO:
    //implement non-start-platforms and platform connections according to future assignments
    public void initGame(final int playerCount) {
        Game game = Model.getInstance().getGame();
        if (playerCount < 2 || playerCount > 4) return;
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player();
            player.setName(DEFAULT_NAMES.get(i))
                    .setColor(DEFAULT_COLORS.get(i))
                    .withUnits(new Unit(), new Unit(), new Unit(), new Unit());
            Platform startPlatform = new Platform();
            startPlatform.setCapacity(STARTING_PLATFORM_CAPACITY)
                    .setPlayer(player)
                    .withUnits(new Unit());
            game.withPlayers(player);
        }

        //should not be changed if playing versus the computer
        //only platforms owned by the current player can be selected as the current platform or reenforced through ui inputs
        game.setCurrentPlayer(game.getPlayers().get(0));
    }

    public void initGame(final int playerCount, final int nonPlayerCharacterCount) {
        initGame(playerCount);
        addNonPlayerCharactersToGameLoop(nonPlayerCharacterCount);
    }

    private void addNonPlayerCharactersToGameLoop(final int nonPlayerCharacterCount) {
        final ArrayList<Player> players = Model.getInstance().getGame().getPlayers();
        final ArrayList<NonPlayerCharacter> nonPlayerCharacters = new ArrayList<>();

        if (nonPlayerCharacterCount >= players.size()) Model.getInstance().getGame().setCurrentPlayer(null);

        for (int i = players.size() - nonPlayerCharacterCount; i < players.size(); i++) {
            nonPlayerCharacters.add(new NonPlayerCharacter(players.get(i)));
        }

        if (gameLoop == null) gameLoop = new GameLoop();
        gameLoop.withNonPlayerCharacters(nonPlayerCharacters);
    }

    public void startGameLoop() {
        if (gameLoop == null) gameLoop = new GameLoop();
        gameLoop.start();
    }

    public void stopGameLoop() {
        if (gameLoop != null) gameLoop.stop();
    }

    public boolean move(final Platform source, final Platform destination) {
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

    public boolean attack(final Platform source, final Platform destination) {
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
