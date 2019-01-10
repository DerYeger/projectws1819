package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.view.ViewBuilder;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;

import java.io.IOException;
import java.util.Objects;

public class PlatformController {

    private static final Paint DEFAULT_PLATFORM_COLOR = Paint.valueOf("#8d8d8d");

    @FXML
    private Ellipse platformShape;

    @FXML
    private HBox meepleBox;

    private Platform platform;

    public void setPlatform(final Platform platform) throws IOException {
        Objects.requireNonNull(platform);
        this.platform = platform;

        meepleBox.getChildren().removeAll();
        for (int i = 1; i <= platform.getCapacity(); i++) {
            meepleBox.getChildren().add(ViewBuilder.buildPlatformMeepleAnchorPane(platform, i));
        }

        addListeners();

        updatePlatformColor();
    }

    private void addListeners() {
        platform.addPropertyChangeListener(Platform.PROPERTY_player, evt -> updatePlatformColor());
    }

    private void updatePlatformColor() {
        Player player = platform.getPlayer();
        if (player != null) {
            String color = player.getColor().substring(2, player.getColor().length() - 2);
            platformShape.setFill(Paint.valueOf("#" + color));
        } else {
            platformShape.setFill(DEFAULT_PLATFORM_COLOR);
        }
    }
}
