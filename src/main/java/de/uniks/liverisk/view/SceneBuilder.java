package de.uniks.liverisk.view;

import de.uniks.liverisk.controller.GameController;
import de.uniks.liverisk.model.Player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Objects;

public class SceneBuilder {

    private static final int DEFAULT_CONTROL_WIDTH = 300;
    private static final int DEFAULT_CONTROL_HEIGHT = 50;
    private static final int DEFAULT_CONTROL_FONT_SIZE = 20;

    public static Scene buildStartScene(final Stage stage, final GameController gc) {
        Objects.requireNonNull(stage);
        Objects.requireNonNull(gc);

        Button twoPlayerButton = new Button("Start 2-player Game");
        Button threePlayerButton = new Button("Start 3-player Game");
        Button fourPlayerButton = new Button("Start 4-player Game");

        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        Label infoLabel = new Label("Please select the number of players for this round.");

        VBox mainBox = new VBox(40);
        VBox labelBox = new VBox(10);
        VBox buttonBox = new VBox(20);

        twoPlayerButton.setPrefWidth(DEFAULT_CONTROL_WIDTH);
        twoPlayerButton.setPrefHeight(DEFAULT_CONTROL_HEIGHT);
        twoPlayerButton.setAlignment(Pos.CENTER);
        twoPlayerButton.setFocusTraversable(false);

        threePlayerButton.setPrefWidth(DEFAULT_CONTROL_WIDTH);
        threePlayerButton.setPrefHeight(DEFAULT_CONTROL_HEIGHT);
        threePlayerButton.setAlignment(Pos.CENTER);
        threePlayerButton.setFocusTraversable(false);

        fourPlayerButton.setPrefWidth(DEFAULT_CONTROL_WIDTH);
        fourPlayerButton.setPrefHeight(DEFAULT_CONTROL_HEIGHT);
        fourPlayerButton.setAlignment(Pos.CENTER);
        fourPlayerButton.setFocusTraversable(false);

        welcomeLabel.setStyle("-fx-font-size: 40");
        infoLabel.setStyle("-fx-font-size: 15");

        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setStyle("-fx-font-size: " + DEFAULT_CONTROL_FONT_SIZE + "; -fx-padding: 40 0 0 0");
        labelBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);

        twoPlayerButton.setOnAction(e -> stage.setScene(buildPlayerEditorScene(stage, gc, 2)));
        threePlayerButton.setOnAction(e -> stage.setScene(buildPlayerEditorScene(stage, gc, 3)));
        fourPlayerButton.setOnAction(e -> stage.setScene(buildPlayerEditorScene(stage, gc, 4)));

        mainBox.getChildren().addAll(labelBox, buttonBox);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);
        buttonBox.getChildren().addAll(twoPlayerButton, threePlayerButton, fourPlayerButton);

        return new Scene(mainBox, 600, 570);
    }

    public static Scene buildPlayerEditorScene(final Stage stage, final GameController gc, final int playerCount) {
        Objects.requireNonNull(stage);
        Objects.requireNonNull(gc);

        gc.initialize(playerCount);

        Button startButton = new Button("Start");

        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        Label infoLabel = new Label("Please choose player names and colors.");

        VBox mainBox = new VBox(40);
        VBox labelBox = new VBox(10);
        VBox editBox = new VBox(20);

        startButton.setPrefWidth(180);
        startButton.setAlignment(Pos.CENTER);
        startButton.setStyle("-fx-font-size: " + DEFAULT_CONTROL_FONT_SIZE);
        startButton.setFocusTraversable(false);

        welcomeLabel.setStyle("-fx-font-size: 40");
        infoLabel.setStyle("-fx-font-size: 15");

        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setStyle("-fx-padding: 40 0 0 0");
        labelBox.setAlignment(Pos.CENTER);
        editBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < playerCount; i++) {

            class PlayerEditHBox extends HBox {

                PlayerEditHBox(Player player) {
                    super(60);

                    TextField nameField = new TextField(gc.getPlayerName(player));
                    ColorPicker colorPicker = new ColorPicker(Color.web(gc.getPlayerColor(player)));

                    nameField.setPrefWidth(DEFAULT_CONTROL_WIDTH);
                    nameField.setPrefHeight(DEFAULT_CONTROL_HEIGHT);
                    nameField.setAlignment(Pos.CENTER);
                    nameField.setFocusTraversable(false);

                    colorPicker.setPrefWidth(DEFAULT_CONTROL_HEIGHT);
                    colorPicker.setPrefHeight(DEFAULT_CONTROL_HEIGHT);
                    colorPicker.getStyleClass().add("button");
                    String color = colorPicker.getValue().toString();
                    color = color.substring(2, color.length() - 2);
                    colorPicker.setStyle("-fx-color-label-visible: false;" +
                            "-fx-background-radius: 30, 30, 30, 30;" +
                            "-fx-background-color: #" + color);
                    colorPicker.setFocusTraversable(false);



                    nameField.textProperty().addListener((observable, oldValue, newValue) -> {
                        gc.setPlayerName(player, newValue);
                        startButton.setDisable(gc.playerConfigurationIsInvalid());
                    });
                    colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                        gc.setPlayerColor(player, newValue.toString());
                        startButton.setDisable(gc.playerConfigurationIsInvalid());
                        String newColor = newValue.toString().substring(2, newValue.toString().length() - 2);
                        colorPicker.setStyle("-fx-color-label-visible: false;" +
                                "-fx-background-radius: 30, 30, 30, 30;" +
                                "-fx-background-color: #" + newColor);
                    });

                    setAlignment(Pos.CENTER);
                    setStyle("-fx-font-size: " + DEFAULT_CONTROL_FONT_SIZE + "; -fx-padding: 0 40 0 40");

                    getChildren().addAll(nameField, colorPicker);
                }
            }

            editBox.getChildren().add(new PlayerEditHBox(gc.getPlayerByNumber(i)));
        }

        mainBox.getChildren().addAll(labelBox, editBox, startButton);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);

        return new Scene(mainBox, 600, 570);
    }
}
