package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Game;
import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.util.GameLoopRunnable;
import de.uniks.liverisk.util.NonPlayerCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameLoop {

    private static final int DEFAULT_DELAY = 5000;
    private static final int DEFAULT_INTERVAL = 10000;
    private static final double SCHEDULE_MODIFIER = 0.5;

    private static GameLoop gameLoop;

    public static GameLoop getInstance() {
        if (gameLoop == null) gameLoop = new GameLoop();
        return gameLoop;
    }

    public void clear() {
        gameLoop = null;
    }

    private ScheduledExecutorService executorService;

    private GameLoop() {
        //singleton
    }

    public void start(final int nonPlayerCharacterCount) {
        if (executorService == null) {
            executorService = Executors.newSingleThreadScheduledExecutor();
            scheduleGameLoopRunnable(nonPlayerCharacterCount);
        }
    }

    private void scheduleGameLoopRunnable(final int nonPlayerCharacterCount) {
        List<NonPlayerCharacter> nonPlayerCharacters = initializeNonPlayerCharacters(nonPlayerCharacterCount);
        executorService.scheduleAtFixedRate(new GameLoopRunnable(nonPlayerCharacters),
                (int) (DEFAULT_DELAY * SCHEDULE_MODIFIER),
                (int) (DEFAULT_INTERVAL * SCHEDULE_MODIFIER),
                TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    private List<NonPlayerCharacter> initializeNonPlayerCharacters(final int nonPlayerCharacterCount) {
        Game game = Model.getInstance().getGame();
        final ArrayList<Player> players = game.getPlayers();
        final ArrayList<NonPlayerCharacter> nonPlayerCharacters = new ArrayList<>();

        //removes ability to use the ui, since all players are npcs
        if (nonPlayerCharacterCount == players.size()) game.setCurrentPlayer(null);

        for (int i = players.size() - nonPlayerCharacterCount; i < players.size(); i++) {
            nonPlayerCharacters.add(new NonPlayerCharacter(players.get(i)));
        }

        return nonPlayerCharacters;
    }
}
