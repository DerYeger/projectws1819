package de.uniks;

import de.uniks.liverisk.controller.GameController;
import de.uniks.liverisk.model.Player;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        buildStartScreen(primaryStage);
        primaryStage.show();
    }

    private Scene buildStartScreen(Stage primaryStage) {
        Button twoPlayerButton = new Button("Start 2-player Game");
        Button threePlayerButton = new Button("Start 3-player Game");
        Button fourPlayerButton = new Button("Start 4-player Game");

        twoPlayerButton.setOnAction(e -> buildPlayerEditorScreen(primaryStage, 2));
        threePlayerButton.setOnAction(e -> buildPlayerEditorScreen(primaryStage, 3));
        fourPlayerButton.setOnAction(e -> buildPlayerEditorScreen(primaryStage, 4));

        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        Label infoLabel = new Label("Please select the number of players for this round.");

        welcomeLabel.setStyle("-fx-font-size: 35");
        infoLabel.setStyle("-fx-font-size: 15");

        VBox mainBox = new VBox(40);
        VBox labelBox = new VBox(10);
        VBox buttonBox = new VBox(20);

        mainBox.setAlignment(Pos.CENTER);
        mainBox.setStyle("-fx-font-size: 20");
        labelBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);

        mainBox.getChildren().addAll(labelBox, buttonBox);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);
        buttonBox.getChildren().addAll(twoPlayerButton, threePlayerButton, fourPlayerButton);

        Scene scene = new Scene(mainBox, 800, 600);
        primaryStage.setScene(scene);
        return scene;
    }

    private Scene buildPlayerEditorScreen(Stage primaryStage, int playerCount) {
        GameController gc = new GameController();
        gc.initialize(playerCount);

        Button startButton = new Button("Start");

        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        Label infoLabel = new Label("Please choose player names and colors.");

        welcomeLabel.setStyle("-fx-font-size: 35");
        infoLabel.setStyle("-fx-font-size: 15");

        VBox mainBox = new VBox(40);
        VBox labelBox = new VBox(10);
        VBox editBox = new VBox(20);

        mainBox.setAlignment(Pos.CENTER);
        mainBox.setStyle("-fx-font-size: 20");
        labelBox.setAlignment(Pos.CENTER);
        editBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < playerCount; i++) {
            PlayerNameField playerNameField =  new PlayerNameField(gc.getGame().getPlayers().get(i));
            PlayerColorPicker playerColorPicker = new PlayerColorPicker(gc.getGame().getPlayers().get(i));

            playerNameField.textProperty().addListener((observable, oldValue, newValue) -> playerNameField.updatePlayerName(newValue));
            playerColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> playerColorPicker.updatePlayerColor(newValue.toString()));

            HBox playerBox = new HBox(20);

            playerBox.setAlignment(Pos.CENTER);
            playerBox.getChildren().addAll(playerNameField, playerColorPicker);
            editBox.getChildren().add(playerBox);
        }

        mainBox.getChildren().addAll(labelBox, editBox, startButton);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);

        Scene scene = new Scene(mainBox, 800, 600);
        primaryStage.setScene(scene);
        return scene;
    }

    private class PlayerNameField extends TextField {
        private Player player;

        public Player getPlayer() {
            return player;
        }

        PlayerNameField(Player player) {
            this.player = player;
            setText(player.getName());
        }

        public void updatePlayerName(String newName) {
            getPlayer().setName(newName);
        }

    }

    private class PlayerColorPicker extends ColorPicker {
        private Player player;

        public Player getPlayer() {
            return player;
        }

        PlayerColorPicker(Player player) {
            this.player = player;
            setValue(Color.web(player.getColor()));
        }

        public void updatePlayerColor(String newColor) {
            getPlayer().setName(newColor);
        }
    }
}
