package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Player;

import java.util.Objects;

public class NonPlayerCharacter {

    private Player player;

    public NonPlayerCharacter(Player player) {
        Objects.requireNonNull(player);
        this.player = player;
    }

    public void update() {
        //TODO implement
        System.out.println("Hello from " + player.getName());
    }
}
