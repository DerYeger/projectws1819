package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

public class NonPlayerCharacter {

    public static final int RANDOM = 0;

    public static final int AGGRESSIVE = 1;

    public static final int PASSIVE = 2;

    private GameController gameController = GameController.getInstance();

    private Player player;

    private int strategy;

    public NonPlayerCharacter(final Player player) {
        this(player, RANDOM);
    }

    public NonPlayerCharacter(final Player player, final int strategy) {
        Objects.requireNonNull(player);
        this.player = player;
        if (strategy == RANDOM) {
            this.strategy = new Random(System.currentTimeMillis()).nextInt(2) + 1;
        } else {
            this.strategy = strategy;
        }
    }

    public void update() {
        reenforce();
        switch (strategy) {
            case AGGRESSIVE:
                aggressiveUpdate();
                break;
            case PASSIVE:
            default:
                aggressiveUpdate();
                break;
        }
    }

    private void reenforce() {
        while (!player.getUnits().isEmpty() && reenforcedPlatform());
    }

    private void aggressiveUpdate() {
        if (attackedHostilePlatform()) return;
        if (movedToEmptyPlatform()) return;
        //if (movedToFrontlinePlatform()) return;
    }

    private void passiveUpdate() {
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
        return player.getPlatforms().parallelStream()
                .filter(platform -> platform.getUnits().size() < platform.getCapacity())
                .min(Comparator.comparingInt((Platform a) -> a.getCapacity() - a.getUnits().size()))
                .orElse(null);
    }

    private Platform getAnyEmptyNeighbor(final Platform platform) {
        Objects.requireNonNull(platform);
        return platform.getNeighbors().parallelStream()
                .filter(neighbor -> neighbor.getPlayer() == null)
                .findAny().orElse(null);
    }

    private Platform getAnyHostileNeighbor(final Platform platform) {
        Objects.requireNonNull(platform);
        return platform.getNeighbors().parallelStream()
                .filter(neighbor -> neighbor.getPlayer() != null && !neighbor.getPlayer().equals(player))
                .findAny().orElse(null);
    }

    //returns a neighbor of the input platform that either has an empty or hostile neighbor itself
    private Platform getAnyFrontlineNeighbor(final Platform platform) {
        Objects.requireNonNull(platform);
        for (Platform neighbor : platform.getNeighbors()) {
            if (neighbor.getPlayer().equals(player) &&
                    neighbor.getNeighbors().parallelStream()
                            .anyMatch(neighborsNeighbor -> neighborsNeighbor.getPlayer() == null ||
                                    !neighborsNeighbor.getPlayer().equals(player))) {
                return neighbor;
            }
        }
        return null;
    }
}
