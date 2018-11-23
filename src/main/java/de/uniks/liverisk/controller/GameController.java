package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.model.Unit;

import java.util.ArrayList;

public class GameController {

    public boolean move(Platform source, Platform destination) {
        if (source == null || destination == null ||
                source.getPlayer() == null || destination.getPlayer() != null && source.getPlayer() != destination.getPlayer() ||
                !source.getNeighbors().contains(destination) ||
                destination.getUnits().size() >= destination.getCapacity() || source.getUnits().size() < 2) return false;

        ArrayList<Unit> unitsToMove = new ArrayList<>(source.getUnits().subList(0, Math.min(destination.getCapacity() - destination.getUnits().size(), source.getUnits().size() - 1)));
        destination.withUnits(unitsToMove);
        destination.setPlayer(destination.getUnits().get(0).getPlayer());
        return true;
    }

    public boolean attack(Platform source, Platform destination) {
        if (source == null || destination == null ||
                source.getPlayer() == null || destination.getPlayer() == null ||
                source.getPlayer() == destination.getPlayer() ||
                !source.getNeighbors().contains(destination) ||
                source.getUnits().size() < 2) return false;

        int lostUnitCount = Math.min(source.getUnits().size() - 1, destination.getUnits().size());

        ArrayList<Unit> attackers = new ArrayList<>(source.getUnits());
        ArrayList<Unit> defenders = new ArrayList<>(destination.getUnits());

        attackers.stream().limit(lostUnitCount).forEach(u -> u.removeYou());
        defenders.stream().limit(lostUnitCount).forEach(u -> u.removeYou());

        if (destination.getUnits().isEmpty()) {
            destination.setPlayer(null);
            move(source, destination);
        }
        return true;
    }

    public boolean reenforce(Platform platform) {
        if (platform == null || platform.getPlayer() == null || platform.getUnits().size() >= platform.getCapacity()) return false;
        int oldUnitCount = platform.getUnits().size();
        platform.getPlayer().getUnits().stream().filter(u -> u.getPlatform() == null)
                .limit(platform.getCapacity() - platform.getUnits().size())
                .forEach(u -> platform.withUnits(u));
        return oldUnitCount != platform.getUnits().size();
    }

}
