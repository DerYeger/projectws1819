package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.*;
import de.uniks.liverisk.util.NonPlayerCharacter;
import de.uniks.liverisk.util.PlatformLayoutGenerator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameController {

    public static final int SPARE_UNITS_LIMIT = 16;

    private static final ArrayList<String> DEFAULT_NAMES = new ArrayList<>(Arrays.asList("Arthur", "Bill", "Charles", "Dutch"));
    private static final ArrayList<String> DEFAULT_COLORS = new ArrayList<>(Arrays.asList("0x336633ff", "0xe64d4dff", "0x4d66ccff", "0xcccc33ff"));

    private static final int PLATFORM_COUNT_MULTIPLIER = 3;

    private static final int LOWER_PLATFORM_CAPACITY_BOUND = 3;
    private static final int UPPER_PLATFORM_CAPACITY_BOUND = 5;
    private static final int STARTING_PLATFORM_CAPACITY = UPPER_PLATFORM_CAPACITY_BOUND;

    private static GameController gameController;

    public static GameController getInstance() {
        if (gameController == null) gameController = new GameController();
        return gameController;
    }

    public static void clear() {
        gameController = null;
    }

    private Game game;

    private GameController() {
        //singleton
        Model.clear();
        game = Model.getInstance().getGame();
    }

    public void initGame(final int playerCount) throws Exception {
        if (playerCount < 2 || playerCount > 4) throw new Exception("Invalid player count");
        initPlayers(playerCount);
        initPlatforms(playerCount * PLATFORM_COUNT_MULTIPLIER);
    }

    private void initPlayers(final int playerCount) {
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
        ArrayList<Platform> platforms = PlatformLayoutGenerator.randomizedPlatformLayout(platformCount);
        game.withPlatforms(platforms);
        setStartingPlatforms();
        setPlatformCapacaties();
    }

    private void setStartingPlatforms() {
        for (Player player : game.getPlayers()) {
            Platform startingPlatform = getAnyPossibleStartingPlatform();
            startingPlatform.setPlayer(player)
                    .withUnits(new Unit());
        }
    }

    private Platform getAnyPossibleStartingPlatform() {
        ArrayList<Platform> platforms = new ArrayList<>(game.getPlatforms());
        Collections.shuffle(platforms);
        return platforms.stream()
                .filter(platform -> platform.getPlayer() == null)
                .findAny().orElse(null);
    }

    private void setPlatformCapacaties() {
        for (Platform platform : game.getPlatforms()) {
            int capacity;
            if (platform.getPlayer() != null) {
                capacity = STARTING_PLATFORM_CAPACITY;
            } else {
                capacity = ThreadLocalRandom.current().nextInt(LOWER_PLATFORM_CAPACITY_BOUND, UPPER_PLATFORM_CAPACITY_BOUND + 1);
            }
            platform.setCapacity(capacity);
        }
    }


    //checks if player names or colors include duplicates
    boolean playerConfigurationIsValid() {
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
