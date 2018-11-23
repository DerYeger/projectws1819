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
        while (destination.getCapacity() - destination.getUnits().size() > 0 && source.getUnits().size() > 1) {
            Unit unit = source.getUnits().get(0);
            source.withoutUnits(unit);
            destination.withUnits(unit);
        }
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
        ArrayList<Unit> lostAttackers = new ArrayList<>(source.getUnits().subList(0, lostUnitCount));
        ArrayList<Unit> lostDefenders = new ArrayList<>(destination.getUnits().subList(0, lostUnitCount));
        source.withoutUnits(lostAttackers)
                .getPlayer().withoutUnits(lostAttackers);
        destination.withoutUnits(lostDefenders)
                .getPlayer().withoutUnits(lostDefenders);

        if (destination.getUnits().isEmpty()) {
            destination.setPlayer(null);
            move(source, destination);
        }
        return true;
    }

    public boolean reenforce(Platform platform) {
        if (platform == null || platform.getPlayer() == null || platform.getUnits().size() >= platform.getCapacity()) return false;
        Player player = platform.getPlayer();
        ArrayList<Unit> spareUnits = new ArrayList<>();

        player.getUnits().stream().filter(u -> u.getPlatform() == null).forEach(u -> spareUnits.add(u));

        if (spareUnits.isEmpty()) return false;

        //Iterator<Unit> it = spareUnits.iterator();
        //while (platform.getCapacity() > platform.getUnits().size() && it.hasNext()) platform.withUnits(it.next());
        int emptySlots = platform.getCapacity() - platform.getUnits().size();
        platform.withUnits(spareUnits.subList(0, emptySlots > spareUnits.size() ? spareUnits.size() : emptySlots));

        return true;
    }

}
