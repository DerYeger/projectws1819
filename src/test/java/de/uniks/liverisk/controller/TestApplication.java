package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.view.GameScreenBuilder;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class TestApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //not part of the assignment, only used to test if implementation works
    @Override
    public void start(Stage primaryStage) throws IOException {
        GameController gc = GameController.getInstance();
        gc.initGame(2, 1);

        Player alice = Model.getInstance().getGame().getPlayers().get(0);
        alice.setName("Alice");
        Platform alicePlatform =  alice.getPlatforms().get(0);

        Player bob = Model.getInstance().getGame().getPlayers().get(1);
        bob.setName("Bob");
        Platform bobPlatform =  bob.getPlatforms().get(0);

        Platform neutralPlatform = new Platform();
        neutralPlatform.setCapacity(3).setGame(Model.getInstance().getGame()).withNeighbors(alicePlatform, bobPlatform);

        Parent alicePlatformParent = GameScreenBuilder.buildPlatformStackPane(alicePlatform);
        Parent bobPlatformParent = GameScreenBuilder.buildPlatformStackPane(bobPlatform);
        Parent neutralPlatformParent = GameScreenBuilder.buildPlatformStackPane(neutralPlatform);
        Parent alicePlayerCardParent = GameScreenBuilder.buildPlayerCardVBox(alice);
        Parent bobPlayerCardParent = GameScreenBuilder.buildPlayerCardVBox(bob);

        neutralPlatform.firePropertyChange(Platform.PROPERTY_player, null , null);

        HBox platformBox = new HBox(10, alicePlatformParent, neutralPlatformParent,bobPlatformParent);
        platformBox.setAlignment(Pos.TOP_CENTER);

        VBox playerCardBox = new VBox(10, alicePlayerCardParent, bobPlayerCardParent);
        playerCardBox.setAlignment(Pos.TOP_CENTER);

        HBox mainBox = new HBox(20, playerCardBox, platformBox);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setStyle("-fx-background-color: #333333");

        Scene scene = new Scene(mainBox, 600, 200);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> gc.stopGameLoop());
        primaryStage.show();

        gc.startGameLoop();
    }
}
