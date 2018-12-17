package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;

import de.uniks.liverisk.view.SceneBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;

import java.io.IOException;


public class PlatformController {

    private static final Paint DEFAULT_PLATFORM_COLOR = Paint.valueOf("#8d8d8d");

    @FXML
    Ellipse platformShape;

    @FXML
    HBox meepleBox;

    private Platform platform;

    public void setPlatform(final Platform platform) {
        this.platform = platform;
        updatePlatformColor();

        meepleBox.getChildren().removeAll();
        for (int i = 0; i < platform.getCapacity(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneBuilder.class.getResource("platformMeeple.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                PlatformMeepleController controller = fxmlLoader.getController();
                controller.init(platform, i);
                meepleBox.getChildren().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
