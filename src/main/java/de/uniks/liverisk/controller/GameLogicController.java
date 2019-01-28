package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Unit;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class GameLogicController {

    private static GameLogicController gameLogicController;

    public static GameLogicController getInstance() {
        if (gameLogicController == null) gameLogicController = new GameLogicController();
        return gameLogicController;
    }

    private Semaphore mutex;

    private GameLogicController() {
        mutex = new Semaphore(1);
    }

    public boolean concurrentMove(final Platform source, final Platform destination) {
        boolean moved = false;
        try {
            try {
                mutex.acquire();
                moved = move(source, destination);
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return moved;
    }

    private boolean move(final Platform source, final Platform destination) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(destination);

        if (!moveIsPossible(source, destination)) return false;

        int emptyDestinationSlots = destination.getCapacity() - destination.getUnits().size();
        int unitsToMoveCount = Math.min(emptyDestinationSlots, source.getUnits().size() - 1);

        ArrayList<Unit> unitsToMove = new ArrayList<>(source.getUnits());
        unitsToMove.stream()
                .limit(unitsToMoveCount)
                .forEach(unit -> unit.setPlatform(destination));

        destination.setPlayer(source.getPlayer());
        return true;
    }

    private boolean moveIsPossible(final Platform source, final Platform destination) {
        return source.getPlayer() != null &&
                (destination.getPlayer() == null || source.getPlayer() == destination.getPlayer()) &&
                source.getNeighbors().contains(destination) &&
                destination.getUnits().size() < destination.getCapacity() &&
                source.getUnits().size() > 1;
    }

    public boolean concurrentAttack(final Platform source, final Platform destination) {
        boolean attacked = false;
        try {
            try {
                mutex.acquire();
                attacked = attack(source, destination);
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return attacked;
    }

    private boolean attack(final Platform source, final Platform destination) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(destination);

        if (!attackIsPossible(source, destination)) return false;

        int lostUnitCount = Math.min(source.getUnits().size() - 1, destination.getUnits().size());

        ArrayList<Unit> attackers = new ArrayList<>(source.getUnits());
        ArrayList<Unit> defenders = new ArrayList<>(destination.getUnits());
        attackers.stream()
                .limit(lostUnitCount)
                .forEach(Unit::removeYou);
        defenders.stream()
                .limit(lostUnitCount)
                .forEach(Unit::removeYou);

        if (destination.getUnits().isEmpty()) {
            destination.setPlayer(null);
            move(source, destination);
        }
        return true;
    }

    private boolean attackIsPossible(final Platform source, final Platform destination) {
        return source.getPlayer() != null &&
                destination.getPlayer() != null &&
                source.getPlayer() != destination.getPlayer() &&
                source.getNeighbors().contains(destination) &&
                source.getUnits().size() > 1;
    }

    public boolean reenforce(final Platform platform) {
        Objects.requireNonNull(platform);

        if (!reenforcementIsPossible(platform)) return false;

        int emptySlots = platform.getCapacity() - platform.getUnits().size();

        ArrayList<Unit> spareUnits = new ArrayList<>(platform.getPlayer().getUnits());
        spareUnits.stream()
                .limit(emptySlots)
                .forEach(unit -> {
                    unit.setPlatform(platform)
                            .setPlayer(null);
                });

        return true;
    }

    private boolean reenforcementIsPossible(final Platform platform) {
        return platform.getPlayer() != null &&
                platform.getUnits().size() < platform.getCapacity() &&
                !platform.getPlayer().getUnits().isEmpty();
    }
}
