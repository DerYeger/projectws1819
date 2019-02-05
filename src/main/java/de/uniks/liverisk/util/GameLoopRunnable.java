package de.uniks.liverisk.util;

import de.uniks.liverisk.controller.GameController;
import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.model.Unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class GameLoopRunnable implements Runnable {

    private static final boolean USE_SCALING_DISTRIBUTION = false;
    private static final int UNITS_PER_TICK_LIMIT = 3;

    private ArrayList<NonPlayerCharacter> nonPlayerCharacters;

    public GameLoopRunnable(final Collection<NonPlayerCharacter> nonPlayerCharacters) {
        if (nonPlayerCharacters != null) {
            this.nonPlayerCharacters = new ArrayList<>(nonPlayerCharacters);
        }
    }

    @Override
    public void run() {
        if (nonPlayerCharacters != null) {
            Collections.shuffle(nonPlayerCharacters);
            updateNonPlayerCharacters();
        }
        addSpareUnitsToPlayers();
        Model.getInstance().saveGame();
    }

    private void addSpareUnitsToPlayers() {
        Model.getInstance().getGame().getPlayers().parallelStream()
                .filter(player -> !player.getPlatforms().isEmpty() && player.getUnits().size() < GameController.SPARE_UNITS_LIMIT)
                .forEach(this::addSpareUnitsToPlayer);
    }

    private void addSpareUnitsToPlayer(final Player player) {
        Objects.requireNonNull(player);
        if (USE_SCALING_DISTRIBUTION) {
            int platformCount = player.getPlatforms().size();
            int earnedUnits = Math.min(UNITS_PER_TICK_LIMIT, platformCount);
            int emptySlots = GameController.SPARE_UNITS_LIMIT - player.getUnits().size();
            int countOfUnitsToAdd = Math.min(earnedUnits, emptySlots);
            for (int i = 0; i < countOfUnitsToAdd; i++) {
                player.withUnits(new Unit());
            }
        } else {
            player.withUnits(new Unit());
        }
    }

    private void updateNonPlayerCharacters() {
        nonPlayerCharacters.stream()
                .filter(npc -> !npc.getPlayer().getPlatforms().isEmpty())
                .forEach(NonPlayerCharacter::update);
    }
}
