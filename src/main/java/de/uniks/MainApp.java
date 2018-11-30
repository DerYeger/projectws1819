package de.uniks;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        Label infoLabel = new Label("Please select the number of players for this round.");
        Button twoPlayerButton = new Button("Start 2-player Game");
        Button threePlayerButton = new Button("Start 2-player Game");
        Button fourPlayerButton = new Button("Start 2-player Game");
        VBox vBox = new VBox(40);
        VBox labelBox = new VBox(5);
        VBox buttonBox = new VBox(20);

        welcomeLabel.setStyle("-fx-font-size: 30");
        infoLabel.setStyle("-fx-font-size: 16");

        vBox.setAlignment(Pos.CENTER);
        labelBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setStyle("-fx-font-size: 20");

        labelBox.getChildren().addAll(welcomeLabel, infoLabel);
        vBox.getChildren().addAll(labelBox, buttonBox);
        buttonBox.getChildren().addAll(twoPlayerButton, threePlayerButton, fourPlayerButton);

        Scene scene = new Scene(vBox, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
