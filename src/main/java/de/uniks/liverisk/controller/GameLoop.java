package de.uniks.liverisk.controller;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameLoop {

    private static final int UNIT_DISTRIBUTION_DELAY = 5000;
    private static final int UNIT_DISTRIBUTION_INTERVAL = 10000;
    private static final double UNIT_DISTRIBUTION_MODIFIER = 0.5;

    private static final int NON_PLAYER_CHARACTERS_UPDATE_DELAY = 5000;
    private static final int NON_PLAYER_CHARACTERS_UPDATE_INTERVAL = 10000;
    private static final double NON_PLAYER_CHARACTERS_UPDATE_MODIFIER = 0.5;

    private ScheduledExecutorService executorService;

    private NonPlayerCharactersUpdateRunnable nonPlayerCharactersUpdateRunnable;

    public void withNonPlayerCharacters(final Collection<NonPlayerCharacter> nonPlayerCharacters) {
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
                (int) (UNIT_DISTRIBUTION_DELAY * UNIT_DISTRIBUTION_MODIFIER),
                (int) (UNIT_DISTRIBUTION_INTERVAL * UNIT_DISTRIBUTION_MODIFIER),
                TimeUnit.MILLISECONDS);
    }

    private void addNonPlayerCharactersRunnable() {
        executorService.scheduleAtFixedRate(nonPlayerCharactersUpdateRunnable,
                (int) (NON_PLAYER_CHARACTERS_UPDATE_DELAY * NON_PLAYER_CHARACTERS_UPDATE_MODIFIER),
                (int) (NON_PLAYER_CHARACTERS_UPDATE_INTERVAL * NON_PLAYER_CHARACTERS_UPDATE_MODIFIER),
                TimeUnit.MILLISECONDS);
    }
}
