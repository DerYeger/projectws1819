package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class GameController {

    private static final ArrayList<String> DEFAULT_NAMES = new ArrayList<>(Arrays.asList("Arthur", "Bill", "Charles", "Dutch"));
    private static final ArrayList<String> DEFAULT_COLORS = new ArrayList<>(Arrays.asList("0x336633ff", "0xe64d4dff", "0x4d66ccff", "0xcccc33ff"));

    private static final int STARTING_PLATFORM_CAPACITY = 5;
    private static final int REGULAR_PLATFORM_CAPACITY = 3;

    //TODO:
    //implement non-start-platforms and platform connections according to future assignments
    public void initGame(int playerCount) {
        Game game = Model.getInstance().getGame();
        if (playerCount < 2 || playerCount > 4) return;
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player();
            player.setName(DEFAULT_NAMES.get(i))
                    .setColor(DEFAULT_COLORS.get(i))
                    .withUnits(new Unit(), new Unit(), new Unit(), new Unit(), new Unit());
            Platform startPlatform = new Platform();
            startPlatform.setCapacity(STARTING_PLATFORM_CAPACITY).setPlayer(player).withUnits(player.getUnits().get(0));
            game.withPlayers(player);
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

    //checks if player names or colors include duplicates
    public boolean playerConfigurationIsInvalid() {
        Game game = Model.getInstance().getGame();
        HashSet<String> nameSet = new HashSet<>();
        HashSet<String> colorSet = new HashSet<>();
        game.getPlayers().stream().filter(p -> !p.getName().isEmpty() && !p.getColor().isEmpty()).forEach(p -> {
            nameSet.add(p.getName());
            colorSet.add(p.getColor());
        });
        return nameSet.size() != game.getPlayers().size() || colorSet.size() != game.getPlayers().size();
    }

    public Player getPlayerByNumber(int number) {
        return Model.getInstance().getGame().getPlayers().get(number);
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
