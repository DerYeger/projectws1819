package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Player;

import javafx.application.Platform;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Objects;

public class PlayerCardMeepleController {

    private Circle meeple;

    private Player player;

    private int slot;

    public void initialize(final Circle meeple, final Player player, final int slot) {
        Objects.requireNonNull(meeple);
        Objects.requireNonNull(player);

        this.meeple = meeple;
        this.player = player;
        this.slot = slot;

        setMeepleColor();
        addListeners();
        updateMeeple();
    }

    private void setMeepleColor() {
        meeple.setFill(Paint.valueOf(player.getColor()));
    }

    private void addListeners() {
        player.addPropertyChangeListener(Player.PROPERTY_platforms, evt -> Platform.runLater(this::updateMeeple));
        player.addPropertyChangeListener(Player.PROPERTY_units, evt -> Platform.runLater(this::updateMeeple));
    }

    private void updateMeeple() {
        meeple.setVisible(player.getUnits().size() >= slot && !player.getPlatforms().isEmpty());
    }
}
