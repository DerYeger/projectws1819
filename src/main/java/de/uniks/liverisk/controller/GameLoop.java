package de.uniks.liverisk.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameLoop {

    private static final int UNIT_DISTRIBUTION_DELAY = 2500;
    private static final int UNIT_DISTRIBUTION_INTERVAL = 10000;
    private static final double UNIT_DISTRIBUTION_MODIFIER = 1;

    private static final int NON_PLAYER_CHARACTERS_UPDATE_DELAY = 5000;
    private static final int NON_PLAYER_CHARACTERS_UPDATE_INTERVAL = 10000;
    private static final double NON_PLAYER_CHARACTERS_UPDATE_MODIFIER = 1;

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

    public void start() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadScheduledExecutor();
            addUnitDistributionRunnable();
            addNonPlayerCharactersRunnable();
        }
    }

    public void stop() {
        executorService.shutdownNow();
    }

    public void addNonPlayerCharacters(final Collection<NonPlayerCharacter> nonPlayerCharacters) {
        if (this.nonPlayerCharacters == null) this.nonPlayerCharacters = new ArrayList<>();
        this.nonPlayerCharacters.addAll(nonPlayerCharacters);
    }

    private void addUnitDistributionRunnable() {
        executorService.scheduleAtFixedRate(new UnitDistributionRunnable(),
                (int) (UNIT_DISTRIBUTION_DELAY * UNIT_DISTRIBUTION_MODIFIER),
                (int) (UNIT_DISTRIBUTION_INTERVAL * UNIT_DISTRIBUTION_MODIFIER),
                TimeUnit.MILLISECONDS);
    }

    private void addNonPlayerCharactersRunnable() {
        executorService.scheduleAtFixedRate(new NonPlayerCharactersUpdaterRunnable(nonPlayerCharacters),
                (int) (NON_PLAYER_CHARACTERS_UPDATE_DELAY * NON_PLAYER_CHARACTERS_UPDATE_MODIFIER),
                (int) (NON_PLAYER_CHARACTERS_UPDATE_INTERVAL * NON_PLAYER_CHARACTERS_UPDATE_MODIFIER),
                TimeUnit.MILLISECONDS);
    }
}
