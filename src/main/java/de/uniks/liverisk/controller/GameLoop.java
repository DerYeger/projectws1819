package de.uniks.liverisk.controller;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameLoop {

    private static final int UNIT_DISTRIBUTION_DELAY = 2500;
    private static final int UNIT_DISTRIBUTION_INTERVAL = 10000;
    private static final double UNIT_DISTRIBUTION_MODIFIER = 0.1;

    private static final int NON_PLAYER_CHARACTERS_UPDATE_DELAY = 5000;
    private static final int NON_PLAYER_CHARACTERS_UPDATE_INTERVAL = 10000;
    private static final double NON_PLAYER_CHARACTERS_UPDATE_MODIFIER = 1;

    private ScheduledExecutorService executorService;

    private NonPlayerCharactersUpdaterRunnable nonPlayerCharactersUpdaterRunnable;

    public GameLoop(Collection<NonPlayerCharacter> nonPlayerCharacters) {
        nonPlayerCharactersUpdaterRunnable = new NonPlayerCharactersUpdaterRunnable(nonPlayerCharacters);
    }

    public GameLoop() {

    }

    public void start() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadScheduledExecutor();
            addUnitDistributionRunnable();
            if (nonPlayerCharactersUpdaterRunnable != null) addNonPlayerCharactersRunnable();
        }
    }

    public void stop() {
        executorService.shutdownNow();
    }

    private void addUnitDistributionRunnable() {
        executorService.scheduleAtFixedRate(new UnitDistributionRunnable(),
                (int) (UNIT_DISTRIBUTION_DELAY * UNIT_DISTRIBUTION_MODIFIER),
                (int) (UNIT_DISTRIBUTION_INTERVAL * UNIT_DISTRIBUTION_MODIFIER),
                TimeUnit.MILLISECONDS);
    }

    private void addNonPlayerCharactersRunnable() {
        executorService.scheduleAtFixedRate(nonPlayerCharactersUpdaterRunnable,
                (int) (NON_PLAYER_CHARACTERS_UPDATE_DELAY * NON_PLAYER_CHARACTERS_UPDATE_MODIFIER),
                (int) (NON_PLAYER_CHARACTERS_UPDATE_INTERVAL * NON_PLAYER_CHARACTERS_UPDATE_MODIFIER),
                TimeUnit.MILLISECONDS);
    }
}
