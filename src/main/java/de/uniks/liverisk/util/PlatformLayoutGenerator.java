package de.uniks.liverisk.util;

import de.uniks.liverisk.controller.GameScreenController;
import de.uniks.liverisk.controller.PlatformController;
import de.uniks.liverisk.model.Platform;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PlatformLayoutGenerator {

    private static final int PLATFORM_SPACING = 50;

    private static final int LOWER_X_POS_BOUND = PLATFORM_SPACING;
    private static final int LOWER_Y_POS_BOUND = PLATFORM_SPACING;
    private static final int UPPER_X_POS_BOUND = GameScreenController.GAME_SCREEN_HEIGHT - PlatformController.PLATFORM_WIDTH - PLATFORM_SPACING;
    private static final int UPPER_Y_POS_BOUND = GameScreenController.GAME_SCREEN_HEIGHT - PlatformController.PLATFORM_HEIGHT - PLATFORM_SPACING;

    public static ArrayList<Platform> randomizedPlatformLayout(final int platformCount) {
        ArrayList<Platform> platforms;
        do {
            platforms = new ArrayList<>();
            for (int i = 0; i < platformCount; i++) {
                platforms.add(new Platform());
            }
            randomizePlatformLocations(platforms);
            randomizePlatformConnections(platforms);
        } while (!graphHasOnlyOneConnectedComponent(platforms));
        return platforms;
    }

    private static void randomizePlatformLocations(final ArrayList<Platform> platforms) {
        for (Platform platform : platforms) {
            do {
                int xPos = ThreadLocalRandom.current().nextInt(LOWER_X_POS_BOUND, UPPER_X_POS_BOUND);
                int yPos = ThreadLocalRandom.current().nextInt(LOWER_Y_POS_BOUND, UPPER_Y_POS_BOUND);
                platform.setXPos(xPos)
                        .setYPos(yPos);
            } while (!platformPlacementIsValid(platforms, platform));
        }
    }

    private static boolean platformPlacementIsValid(final ArrayList<Platform> platforms, final Platform platform) {
        return platforms.parallelStream()
                .noneMatch(p -> p.getXPos() != 0
                        && !p.equals(platform)
                        && platformsOverlap(platform, p));
    }

    private static boolean platformsOverlap(final Platform a, final Platform b) {
        if (a.getYPos() + PlatformController.PLATFORM_HEIGHT + PLATFORM_SPACING < b.getYPos()
                || a.getYPos() > b.getYPos() + PlatformController.PLATFORM_HEIGHT + PLATFORM_SPACING) {
            return false;
        }
        if (a.getXPos() + PlatformController.PLATFORM_WIDTH + PLATFORM_SPACING < b.getXPos()
                || a.getXPos() > b.getXPos() + PlatformController.PLATFORM_WIDTH + PLATFORM_SPACING) {
            return false;
        }
        return true;
    }

    //TODO ensure that there are not multiple sub graphs
    private static void randomizePlatformConnections(final ArrayList<Platform> platforms) {
        for (Platform platform : platforms) {
            List<Platform> closePlatforms = getTwoClosestPlatforms(platforms, platform);
            platform.withNeighbors(closePlatforms);
        }
    }

    private static List<Platform> getTwoClosestPlatforms(final ArrayList<Platform> platforms, final Platform platform) {
        ArrayList<Platform> platformList = new ArrayList<>(platforms);
        platformList.remove(platform);
        platformList.sort(Comparator.comparingDouble(p -> getDistance(p, platform)));
        return platformList.subList(0, 2);
    }

    private static double getDistance(final Platform a, final Platform b) {
        double xComponent = b.getXPos() - a.getXPos();
        double yComponent = b.getYPos() - a.getYPos();
        return Math.sqrt(Math.pow(xComponent, 2) + Math.pow(yComponent, 2));
    }

    private static boolean graphHasOnlyOneConnectedComponent(final ArrayList<Platform> platforms) {
        HashSet<Platform> visitedPlatforms = new HashSet<>();
        Queue<Platform> platformQueue = new LinkedList<>();
        platformQueue.offer(platforms.get(0));
        while (!platformQueue.isEmpty()) {
            Platform platform = platformQueue.poll();
            for (Platform neighbor : platform.getNeighbors()) if (!visitedPlatforms.contains(neighbor)) platformQueue.offer(neighbor);
            visitedPlatforms.add(platform);
        }
        return visitedPlatforms.size() == platforms.size();
    }
}
