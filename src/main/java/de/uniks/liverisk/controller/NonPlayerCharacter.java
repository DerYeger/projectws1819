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
        if (reenforcedPlatform()) return;

        if (movedToEmptyPlatform()) return;

        if (attackedHostilePlatform()) return;

        //if (movedToFrontlinePlatform()) return;
    }

    //TODO improve
    private boolean reenforcedPlatform() {
        Platform platform = getAnyReenforcablePlatform();
        return platform != null && gameController.reenforce(platform);
    }

    //attempts to move to an empty platform and returns true if such a platform was found
    private boolean movedToEmptyPlatform() {
        for (Platform platform : player.getPlatforms()) {
            Platform emptyNeighbor = getAnyEmptyNeighbor(platform);
            if (platform.getUnits().size() > 1 && emptyNeighbor != null) {
                return gameController.move(platform, emptyNeighbor);
            }
        }
        return false;
    }

    private boolean attackedHostilePlatform() {
        for (Platform platform : player.getPlatforms()) {
            Platform hostileNeighbor = getAnyHostileNeighbor(platform);
            if (platform.getUnits().size() > 1 && hostileNeighbor != null) {
                System.out.println("attacked");
                return gameController.attack(platform, hostileNeighbor);
            }
        }
        return false;
    }

    private boolean movedToFrontlinePlatform() {
        for (Platform platform : player.getPlatforms()) {
            Platform frontlinePlatform = getAnyFrontlineNeighbor(platform);
            if (platform.getUnits().size() > 1 && frontlinePlatform != null) {
                return gameController.move(platform, frontlinePlatform);
            }
        }
        return false;
    }

    private Platform getAnyReenforcablePlatform() {
        return player.getPlatforms().stream()
                .filter(platform -> platform.getUnits().size() < platform.getCapacity())
                .min(Comparator.comparingInt((Platform a) -> a.getCapacity() - a.getUnits().size()))
                .orElse(null);
    }

    private Platform getAnyEmptyNeighbor(final Platform platform) {
        Objects.requireNonNull(platform);
        return platform.getNeighbors().stream()
                .filter(neighbor -> neighbor.getPlayer() == null)
                .findAny().orElse(null);
    }

    private Platform getAnyHostileNeighbor(final Platform platform) {
        Objects.requireNonNull(platform);
        return platform.getNeighbors().stream()
                .filter(neighbor -> neighbor.getPlayer() != null && !neighbor.getPlayer().equals(player))
                .findAny().orElse(null);
    }

    //returns a neighbor of the input platform that either has an empty or hostile neighbor itself
    private Platform getAnyFrontlineNeighbor(final Platform platform) {
        Objects.requireNonNull(platform);
        for (Platform neighbor : platform.getNeighbors()) {
            if (neighbor.getPlayer().equals(player) &&
                    neighbor.getNeighbors().stream()
                            .anyMatch(neighborsNeighbor -> neighborsNeighbor.getPlayer() == null ||
                                    !neighborsNeighbor.getPlayer().equals(player))) {
                return neighbor;
            }
        }
        return null;
    }
}
