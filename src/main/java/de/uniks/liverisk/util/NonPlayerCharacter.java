package de.uniks.liverisk.util;

import de.uniks.liverisk.controller.GameLogicController;
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

    private static int getRandomInt() {
        if (random == null) random = new Random(System.currentTimeMillis());
        return random.nextInt(BEHAVIOUR_COUNT);
    }

    private Player player;

    public NonPlayerCharacter(final Player player) {
        Objects.requireNonNull(player);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
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
        if (reenforcedPlatform()) { return; }
        if (movedToEmptyPlatform()) { return; }
//      if (movedToFrontlinePlatform()) { return; }
        attackedHostilePlatform();
    }

    private void aggressiveUpdate() {
        if (attackedHostilePlatform()) { return; }
        if (movedToEmptyPlatform()) { return; }
//      if (movedToFrontlinePlatform()) {
        reenforcedPlatform();
    }

    private void passiveUpdate() {
        if (movedToEmptyPlatform()) { return; }
//      if (movedToFrontlinePlatform()) { return; }
        if (attackedHostilePlatform()) { return; }
        reenforcedPlatform();
    }

    private void defensiveUpdate() {
//      if (movedToFrontlinePlatform()) { return; }
        if (movedToEmptyPlatform()) { return; }
        if (attackedHostilePlatform()) { return; }
        reenforcedPlatform();
    }

    //TODO improve
    private boolean reenforcedPlatform() {
        Platform platform = getAnyReenforcablePlatform();
        return platform != null && GameLogicController.getInstance().reenforce(platform);
    }

    //attempts to move to an empty platform and returns true if successful
    private boolean movedToEmptyPlatform() {
        for (Platform platform : player.getPlatforms()) {
            if (platform.getUnits().size() < 2) continue;
            Platform emptyNeighbor = getAnyEmptyNeighbor(platform);
            if (emptyNeighbor == null) continue;
            return GameLogicController.getInstance().concurrentMove(platform, emptyNeighbor);
        }
        return false;
    }

    //attempts to attack to a hostile platform and returns true if successful
    private boolean attackedHostilePlatform() {
        for (Platform platform : player.getPlatforms()) {
            if (platform.getUnits().size() < 2) continue;
            Platform hostileNeighbor = getAnyHostileNeighbor(platform);
            if (hostileNeighbor == null) continue;
            return GameLogicController.getInstance().concurrentAttack(platform, hostileNeighbor);
        }
        return false;
    }

    //attempts to move to an allied platform and returns true if successful
    private boolean movedToFrontlinePlatform() {
        for (Platform platform : player.getPlatforms()) {
            if (platform.getUnits().size() < 2) continue;
            Platform frontlinePlatform = getAnyFrontlineNeighbor(platform);
            if (frontlinePlatform == null) continue;
            return GameLogicController.getInstance().concurrentMove(platform, frontlinePlatform);
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
