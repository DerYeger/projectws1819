package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.model.Unit;

public class UnitDistributionRunnable implements Runnable {

    private static final boolean USE_SCALING_DISTRIBUTION = true;
    private static final int MAX_UNITS_PER_TICK = 3;

    @Override
    public void run() {
        addSpareUnitsToPlayers();
    }

    private void addSpareUnitsToPlayers() {
        Model.getInstance().getGame().getPlayers().parallelStream()
                .filter(player -> !player.getPlatforms().isEmpty() && player.getUnits().size() < GameController.MAX_SPARE_UNIT_COUNT)
                .forEach(this::addSpareUnitsToPlayer);
    }

    private void addSpareUnitsToPlayer(final Player player) {
        if (USE_SCALING_DISTRIBUTION) {
            int platformCount = player.getPlatforms().size();
            int countOfUnitsToAdd = Math.min(MAX_UNITS_PER_TICK, platformCount);
            for (int i = 0; i < countOfUnitsToAdd; i++) {
                player.withUnits(new Unit());
            }
        } else {
            player.withUnits(new Unit());
        }
    }
}
