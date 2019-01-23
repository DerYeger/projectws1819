package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;

import java.util.*;

public class NonPlayerCharacter {

    private static final int BEHAVIOUR_COUNT = 4;

    private static final int REENFORCING = 0;
    private static final int AGGRESSIVE = 1;
    private static final int PASSIVE = 2;
    private static final int DEFENSIVE = 3;

    private static Random random;

    private GameController gameController = GameController.getInstance();

    private Player player;

    private static int getRandomInt() {
        if (random == null) random = new Random(System.currentTimeMillis());
        return random.nextInt(BEHAVIOUR_COUNT);
    }

    public NonPlayerCharacter(final Player player) {
        Objects.requireNonNull(player);
        this.player = player;
    }

    public synchronized void update()  {

        int behaviour = getRandomInt();
        switch (behaviour) {
            case REENFORCING:
                reenforcingUpdate();
                break;
            case AGGRESSIVE:
                aggressiveUpdate();
                break;
            case PASSIVE:
                passiveUpdate();
                break;
            case DEFENSIVE:
                defensiveUpdate();
                break;
            default:
                reenforcingUpdate();
                break;
        }
    }

    private void reenforcingUpdate() {
        if (reenforcedPlatform()) {
            System.out.println(player.getName() + " reenforced.");
        } else if (movedToEmptyPlatform()) {
            System.out.println(player.getName() + " moved to empty platform.");
//        } else if (movedToFrontlinePlatform()) {
//            System.out.println(player.getName() + " moved to front.");
        } else if (attackedHostilePlatform()) {
            System.out.println(player.getName() + " attacked.");
        } else {
        System.out.println(player.getName() + " did nothing!");
        }
    }

    private void aggressiveUpdate() {
        if (attackedHostilePlatform()) {
            System.out.println(player.getName() + " attacked.");
        } else if (movedToEmptyPlatform()) {
            System.out.println(player.getName() + " moved to empty platform.");
//        } else if (movedToFrontlinePlatform()) {
//            System.out.println(player.getName() + " moved to front.");
        } else if (reenforcedPlatform()) {
            System.out.println(player.getName() + " reenforced.");
        } else {
            System.out.println(player.getName() + " did nothing!");
        }
    }

    private void passiveUpdate() {
        if (movedToEmptyPlatform()) {
            System.out.println(player.getName() + " moved to empty platform.");
//        } else if (movedToFrontlinePlatform()) {
//            System.out.println(player.getName() + " moved to front.");
        } else if (attackedHostilePlatform()) {
            System.out.println(player.getName() + " attacked.");
        } else if (reenforcedPlatform()) {
            System.out.println(player.getName() + " reenforced.");
        } else {
            System.out.println(player.getName() + " did nothing!");
        }
    }

    private void defensiveUpdate() {
//        if (movedToFrontlinePlatform()) {
//            System.out.println(player.getName() + " moved to front.");
//        } else
        if (movedToEmptyPlatform()) {
            System.out.println(player.getName() + " moved to empty platform.");
        } else if (attackedHostilePlatform()) {
            System.out.println(player.getName() + " attacked.");
        } else if (reenforcedPlatform()) {
            System.out.println(player.getName() + " reenforced.");
        } else {
            System.out.println(player.getName() + " did nothing!");
        }
    }

    //TODO improve
    private boolean reenforcedPlatform() {
        Platform platform = getAnyReenforcablePlatform();
        return platform != null && gameController.reenforce(platform);
    }

    //attempts to move to an empty platform and returns true if successful
    private boolean movedToEmptyPlatform() {
        for (Platform platform : player.getPlatforms()) {
            if (platform.getUnits().size() < 2) continue;
            Platform emptyNeighbor = getAnyEmptyNeighbor(platform);
            if (emptyNeighbor == null) continue;
            return gameController.concurrentMove(platform, emptyNeighbor);
        }
        return false;
    }

    //attempts to attack to a hostile platform and returns true if successful
    private boolean attackedHostilePlatform() {
        for (Platform platform : player.getPlatforms()) {
            if (platform.getUnits().size() < 2) continue;
            Platform hostileNeighbor = getAnyHostileNeighbor(platform);
            if (hostileNeighbor == null) continue;
            return gameController.concurrentAttack(platform, hostileNeighbor);
        }
        return false;
    }

    //attempts to move to an allied platform and returns true if successful
    private boolean movedToFrontlinePlatform() {
        for (Platform platform : player.getPlatforms()) {
            if (platform.getUnits().size() < 2) continue;
            Platform frontlinePlatform = getAnyFrontlineNeighbor(platform);
            if (frontlinePlatform == null) continue;
            return gameController.concurrentMove(platform, frontlinePlatform);
        }
        return false;
    }

    private Platform getAnyReenforcablePlatform() {
        return player.getPlatforms().parallelStream()
                .filter(platform -> platform.getUnits().size() < platform.getCapacity())
                .min(Comparator.comparingInt((Platform platform) -> platform.getCapacity() - platform.getUnits().size()))
                .orElse(null);
    }

    private Platform getAnyEmptyNeighbor(final Platform platform) {
        Objects.requireNonNull(platform);
        List<Platform> potentialTargets = platform.getNeighbors();
        Collections.shuffle(potentialTargets);
        return potentialTargets.parallelStream()
                .filter(neighbor -> neighbor.getPlayer() == null)
                .findAny().orElse(null);
    }

    private Platform getAnyHostileNeighbor(final Platform platform) {
        Objects.requireNonNull(platform);
        List<Platform> potentialTargets = platform.getNeighbors();
        Collections.shuffle(potentialTargets);
        return potentialTargets.parallelStream()
                .filter(neighbor -> neighbor.getPlayer() != null && !neighbor.getPlayer().equals(player))
                .findAny().orElse(null);
    }

    private Platform getAnyFrontlineNeighbor(final Platform platform) {
        Objects.requireNonNull(platform);
        List<Platform> potentialTargets = platform.getNeighbors();
        Collections.shuffle(potentialTargets);
        return potentialTargets.parallelStream()
                .filter(neighbor -> neighbor.getPlayer().equals(player) &&
                        getAnyEmptyNeighbor(platform) == null && getAnyHostileNeighbor(platform) == null &&
                        (getAnyEmptyNeighbor(neighbor) != null || getAnyHostileNeighbor(neighbor) != null))
                .findAny().orElse(null);
    }
}
