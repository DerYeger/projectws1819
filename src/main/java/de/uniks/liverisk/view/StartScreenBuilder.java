package de.uniks.liverisk.view;

import de.uniks.liverisk.controller.StartScreenController;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class StartScreenBuilder {

    public static Parent getStartScreen(final Stage stage) {
        return buildStartScreenVBox(stage);
    }

    private static VBox buildStartScreenVBox(final Stage stage) {
        Objects.requireNonNull(stage);

        Button twoPlayerButton = new Button("Start 2-player Game");
        Button threePlayerButton = new Button("Start 3-player Game");
        Button fourPlayerButton = new Button("Start 4-player Game");
        Button loadSaveGameButton = new Button("Load Saved Game");

        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        Label infoLabel = new Label("Please select the number of players for this round.");

        VBox startScreenVBox = new VBox(40);
        VBox labelBox = new VBox(10);
        VBox buttonBox = new VBox(20);

        new StartScreenController().initialize(stage, loadSaveGameButton, twoPlayerButton, threePlayerButton, fourPlayerButton);

        welcomeLabel.setStyle("-fx-font-size: 40");
        infoLabel.setStyle("-fx-font-size: 15");

        startScreenVBox.setAlignment(Pos.TOP_CENTER);
        startScreenVBox.setStyle("-fx-padding: 40 0 0 0");
        labelBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);

        startScreenVBox.getChildren().addAll(labelBox, buttonBox);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);
        buttonBox.getChildren().addAll(twoPlayerButton, threePlayerButton, fourPlayerButton, loadSaveGameButton);

        return startScreenVBox;
    }
}
