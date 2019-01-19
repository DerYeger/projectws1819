package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;

import java.util.Comparator;
import java.util.Objects;

public class NonPlayerCharacter {

    private GameController gameController = GameController.getInstance();

    private Player player;

    public NonPlayerCharacter(Player player) {
        Objects.requireNonNull(player);
        this.player = player;
    }

    public void update() {
        //TODO implement
        System.out.println("before reenforce");
        if (reenforcedMostExposedPlatform()) return;
        System.out.println("before move");
        if (moveToEmptyPlatform()) return;
        System.out.println("before attack");
        attackHostilePlatform();
    }

    //TODO improve
    private boolean reenforcedMostExposedPlatform() {
        Platform platformToReenforce = player.getPlatforms().stream()
                .filter(platform -> platform.getUnits().size() < platform.getCapacity())
                .min(Comparator.comparingInt((Platform a) -> a.getUnits().size()))
                .orElse(null);
        return platformToReenforce != null && gameController.reenforce(platformToReenforce);
    }

    //attempts to move to an empty platform and returns true if such a platform was found
    private boolean moveToEmptyPlatform() {
        for (Platform platform : player.getPlatforms()) {
            Platform emptyNeighbor = getAnyEmptyNeighbor(platform);
            if (platform.getUnits().size() > 1 && emptyNeighbor != null) {
                return gameController.move(platform, emptyNeighbor);
            }
        }
        return false;
    }

    private boolean attackHostilePlatform() {
        for (Platform platform : player.getPlatforms()) {
            Platform hostileNeighbor = getAnyHostileNeighbor(platform);
            if (platform.getUnits().size() > 1 && hostileNeighbor != null) {
                return gameController.attack(platform, hostileNeighbor);
            }
        }
        return false;
    }

    private Platform getAnyEmptyNeighbor(final Platform platform) {
        return platform.getNeighbors().stream()
                .filter(neighbor -> neighbor.getPlayer() == null)
                .findAny().orElse(null);
    }

    private Platform getAnyHostileNeighbor(final Platform platform) {
        return platform.getNeighbors().stream()
                .filter(neighbor -> neighbor.getPlayer() != null && !neighbor.getPlayer().equals(player))
                .findAny().orElse(null);
    }
}
