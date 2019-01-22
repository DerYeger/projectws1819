package de.uniks.liverisk.view;

import de.uniks.liverisk.controller.PlayerEditHBoxController;
import de.uniks.liverisk.controller.PlayerEditorScreenController;
import de.uniks.liverisk.model.Player;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class PlayerEditorScreenBuilder {

    public static Parent getPlayerEditorScreen(final Stage stage) {
        return buildPlayerEditorScreenVBox(stage);
    }

    private static VBox buildPlayerEditorScreenVBox(final Stage stage) {
        Objects.requireNonNull(stage);

        Button startButton = new Button("Start");

        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        Label infoLabel = new Label("Please choose player names and colors.");

        VBox playerEditorScreenVBox = new VBox(40);
        VBox labelBox = new VBox(10);
        VBox editBox = new VBox(20);

        new PlayerEditorScreenController().initialize(stage, editBox, startButton);

        startButton.setStyle("-fx-pref-width: 140");

        welcomeLabel.setStyle("-fx-font-size: 40");
        infoLabel.setStyle("-fx-font-size: 15");

        playerEditorScreenVBox.setAlignment(Pos.TOP_CENTER);
        playerEditorScreenVBox.setStyle("-fx-padding: 40 0 0 0");
        labelBox.setAlignment(Pos.CENTER);
        editBox.setAlignment(Pos.CENTER);

        playerEditorScreenVBox.getChildren().addAll(labelBox, editBox, startButton);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);

        return playerEditorScreenVBox;
    }

    public static HBox buildPlayerEditorHBox(final Player player) {
        Objects.requireNonNull(player);

        HBox playerEditorHBox = new HBox(60);
        TextField nameField = new TextField();
        ColorPicker colorPicker = new ColorPicker();

        new PlayerEditHBoxController().setPlayer(player, nameField, colorPicker);

        colorPicker.getStyleClass().add("button");

        playerEditorHBox.setAlignment(Pos.CENTER);
        playerEditorHBox.setStyle("-fx-padding: 0 40 0 40");

        playerEditorHBox.getChildren().addAll(nameField, colorPicker);

        return playerEditorHBox;
    }
}
