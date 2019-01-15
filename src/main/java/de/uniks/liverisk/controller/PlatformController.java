package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.view.GameScreenBuilder;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.io.IOException;
import java.util.Objects;

public class PlatformController {

    private static final Color DEFAULT_PLATFORM_COLOR = Color.valueOf("#8d8d8d");

    @FXML
    private Ellipse platformShape;

    @FXML
    private HBox meepleBox;

    private Platform platform;

    public void setPlatform(final Platform platform) throws IOException {
        Objects.requireNonNull(platform);
        this.platform = platform;

        addPlatformMeeples();
        addListeners();
        updatePlatformColor();
    }

    private void addPlatformMeeples() throws IOException {
        for (int i = 1; i <= platform.getCapacity(); i++) {
            meepleBox.getChildren().add(GameScreenBuilder.buildPlatformMeepleAnchorPane(platform, i));
        }
    }

    private void addListeners() {
        platform.addPropertyChangeListener(Platform.PROPERTY_player, evt -> updatePlatformColor());
    }

    private void updatePlatformColor() {
        Player player = platform.getPlayer();
        if (player != null) {
            platformShape.setFill(Color.valueOf(player.getColor()));
        } else {
            platformShape.setFill(DEFAULT_PLATFORM_COLOR);
        }
    }
}
