package de.uniks.liverisk.controller;

import de.uniks.liverisk.util.GameLoopRunnable;
import de.uniks.liverisk.util.NonPlayerCharacter;

import java.util.ArrayList;
import java.util.Collection;
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

    private ArrayList<NonPlayerCharacter> nonPlayerCharacters;

    private GameLoop() {
        //singleton
    }

    public void addNonPlayerCharacters(final Collection<NonPlayerCharacter> nonPlayerCharacters) {
        if (this.nonPlayerCharacters == null) this.nonPlayerCharacters = new ArrayList<>();
        this.nonPlayerCharacters.addAll(nonPlayerCharacters);
    }

    public void start() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadScheduledExecutor();
            scheduleGameLoopRunnable();
        }
    }

    private void scheduleGameLoopRunnable() {
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
}
