package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Game;
import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Unit;

public class UnitDistributionRunnable implements Runnable {

    private static final int MAX_SPARE_UNIT_COUNT = 16;

    @Override
    public void run() {
        Game game = Model.getInstance().getGame();
        game.getPlayers().stream()
                .filter(player -> !player.getPlatforms().isEmpty() && player.getUnits().size() < MAX_SPARE_UNIT_COUNT)
                .forEach(player -> player.withUnits(new Unit()));
    }
}
