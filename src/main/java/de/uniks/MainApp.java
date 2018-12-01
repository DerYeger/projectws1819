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

import java.util.Objects;

public class MainApp extends Application {

    private GameController gc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        buildStartScreen(primaryStage);
        primaryStage.show();
    }


    private void buildStartScreen(Stage primaryStage) {
        Objects.requireNonNull(primaryStage);
        Button twoPlayerButton = new Button("Start 2-player Game");
        Button threePlayerButton = new Button("Start 3-player Game");
        Button fourPlayerButton = new Button("Start 4-player Game");

        twoPlayerButton.setOnAction(e -> buildPlayerEditorScreen(primaryStage, 2));
        threePlayerButton.setOnAction(e -> buildPlayerEditorScreen(primaryStage, 3));
        fourPlayerButton.setOnAction(e -> buildPlayerEditorScreen(primaryStage, 4));

        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        welcomeLabel.setStyle("-fx-font-size: 35");

        Label infoLabel = new Label("Please select the number of players for this round.");
        infoLabel.setStyle("-fx-font-size: 15");

        VBox mainBox = new VBox(40);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setStyle("-fx-font-size: 20");

        VBox labelBox = new VBox(10);
        labelBox.setAlignment(Pos.CENTER);

        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        mainBox.getChildren().addAll(labelBox, buttonBox);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);
        buttonBox.getChildren().addAll(twoPlayerButton, threePlayerButton, fourPlayerButton);

        Scene scene = new Scene(mainBox, 800, 600);
        primaryStage.setScene(scene);
    }

    private void buildPlayerEditorScreen(Stage primaryStage, int playerCount) {
        Objects.requireNonNull(primaryStage);
        gc = new GameController();
        gc.initialize(playerCount);

        Button startButton = new Button("Start");

        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        welcomeLabel.setStyle("-fx-font-size: 35");

        Label infoLabel = new Label("Please choose player names and colors.");
        infoLabel.setStyle("-fx-font-size: 15");

        VBox mainBox = new VBox(40);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setStyle("-fx-font-size: 20");

        VBox labelBox = new VBox(10);
        labelBox.setAlignment(Pos.CENTER);

        VBox editBox = new VBox(20);
        editBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < playerCount; i++) {

            class PlayerEditHBox extends HBox{
                private Player player;
                private TextField playerNameField;
                private ColorPicker playerColorPicker;


                PlayerEditHBox(double spacing, Player player) {
                    super(spacing);
                    this.player = player;
                    initialize();
                }

                private void initialize() {
                    playerNameField = new TextField(gc.getPlayerName(player));
                    playerColorPicker = new ColorPicker(Color.web(gc.getPlayerColor(player)));
                    playerNameField.textProperty().addListener((observable, oldValue, newValue) -> gc.setPlayerName(player, newValue));
                    playerColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> gc.setPlayerColor(player, newValue.toString()));
                    getChildren().addAll(playerNameField, playerColorPicker);
                    setAlignment(Pos.CENTER);
                }
            }

            editBox.getChildren().add(new PlayerEditHBox(20, gc.getPlayerByNumber(i)));
        }

        mainBox.getChildren().addAll(labelBox, editBox, startButton);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);

        Scene scene = new Scene(mainBox, 800, 600);
        primaryStage.setScene(scene);
    }


}
