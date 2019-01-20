package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Game;
import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.model.Unit;

public class UnitDistributionRunnable implements Runnable {

    private static final int MAX_SPARE_UNIT_COUNT = 16;

    private static final int MAX_UNITS_TO_ADD_PER_TICK = 3;

    @Override
    public void run() {
        Game game = Model.getInstance().getGame();
        game.getPlayers().stream()
                .filter(player -> !player.getPlatforms().isEmpty() && player.getUnits().size() < MAX_SPARE_UNIT_COUNT)
                .forEach(this::addSpareUnits);
    }

    private void addSpareUnits(final Player player) {
        int platformCount = player.getPlatforms().size();
        //int countOfUnitsToAdd = (int) ((Math.log(platformCount) + 1) / Math.log(2));
        int countOfUnitsToAdd = Math.min(MAX_UNITS_TO_ADD_PER_TICK, platformCount);

        for (int i = 0; i < countOfUnitsToAdd; i++) {
            player.withUnits(new Unit());
        }
    }
}
