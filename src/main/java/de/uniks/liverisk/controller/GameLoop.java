package de.uniks.liverisk.controller;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameLoop {

    private static final int UNIT_DISTRIBUTION_DELAY = 3000;
    private static final int UNIT_DISTRIBUTION_INTERVAL = 3000;

    private static final int NON_PLAYER_CHARACTERS_UPDATE_DELAY = 2000;
    private static final int NON_PLAYER_CHARACTERS_UPDATE_INTERVAL = 2000;

    private ScheduledExecutorService executorService;

    private NonPlayerCharactersUpdateRunnable nonPlayerCharactersUpdateRunnable;

    public void withNonPlayerCharacters(Collection<NonPlayerCharacter> nonPlayerCharacters) {
        Objects.requireNonNull(nonPlayerCharacters);
        nonPlayerCharactersUpdateRunnable = new NonPlayerCharactersUpdateRunnable(nonPlayerCharacters);
    }

    public void start() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadScheduledExecutor();
            addUnitDistributionRunnable();
            if (nonPlayerCharactersUpdateRunnable != null) addNonPlayerCharactersRunnable();
        }
    }

    public void stop() {
        executorService.shutdownNow();
    }

    private void addUnitDistributionRunnable() {
        executorService.scheduleAtFixedRate(new UnitDistributionRunnable(),
                UNIT_DISTRIBUTION_DELAY, UNIT_DISTRIBUTION_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void addNonPlayerCharactersRunnable() {
        executorService.scheduleAtFixedRate(nonPlayerCharactersUpdateRunnable,
                NON_PLAYER_CHARACTERS_UPDATE_DELAY, NON_PLAYER_CHARACTERS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }
}
