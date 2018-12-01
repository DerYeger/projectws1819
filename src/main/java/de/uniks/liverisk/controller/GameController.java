package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Game;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.model.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class GameController {

    private static final ArrayList<String> defaultNames = new ArrayList<>(Arrays.asList("Arthur", "Bill", "Charles", "Dutch"));
    private static final ArrayList<String> defaultColors = new ArrayList<>(Arrays.asList("green", "red", "blue", "yellow"));

    private Game game;

    public Game getGame() {
        return game;
    }

    public void initialize(int playerCount) {
        if (game != null || playerCount < 2 || playerCount > 4) return;
        game = new Game();
        for (int i = 0; i < playerCount; i++) {
            game.withPlayers(new Player().setName(defaultNames.get(i)).setColor(defaultColors.get(i)));
        }
    }

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
                .forEach(u -> u.setPlatform(platform));
        return oldUnitCount != platform.getUnits().size();
    }

    public boolean playerConfigurationIsInvalid() {
        //checks if player names include duplicates
        HashSet<String> nameSet = new HashSet<>();
        getGame().getPlayers().stream().filter(p -> !p.getName().isEmpty()).forEach(p -> nameSet.add(p.getName()));
        if (nameSet.size() != getGame().getPlayers().size()) return true;

        //checks if player colors include duplicates
        HashSet<String> colorSet = new HashSet<>();
        getGame().getPlayers().stream().filter(p -> !p.getColor().isEmpty()).forEach(p -> colorSet.add(p.getColor()));
        return colorSet.size() != getGame().getPlayers().size();
    }

    public Player getPlayerByNumber(int number) {
        return getGame().getPlayers().get(number);
    }

    public String getPlayerName(Player player) {
        return player.getName();
    }

    public void setPlayerName(Player player, String name) {
        player.setName(name);
    }

    public String getPlayerColor(Player player) {
        return player.getColor();
    }

    public void setPlayerColor(Player player, String color) {
        player.setColor(color);
    }

}
