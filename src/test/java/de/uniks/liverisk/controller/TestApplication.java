package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.view.GameScreenBuilder;

import javafx.application.Application;
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
        gc.initGame(2, 2);

        Player alice = Model.getInstance().getGame().getPlayers().get(0);
        alice.setName("Alice");
        Platform alicePlatform =  alice.getPlatforms().get(0);

        Player bob = Model.getInstance().getGame().getPlayers().get(1);
        bob.setName("Bob");
        Platform bobPlatform =  bob.getPlatforms().get(0);

        Platform nL = new Platform();
        Platform nT = new Platform();
        Platform nM = new Platform();
        Platform nB = new Platform();
        Platform nR = new Platform();

        nL.setCapacity(3).setGame(Model.getInstance().getGame());
        nR.setCapacity(3).setGame(Model.getInstance().getGame());
        nT.setCapacity(3).setGame(Model.getInstance().getGame());
        nM.setCapacity(3).setGame(Model.getInstance().getGame());
        nB.setCapacity(3).setGame(Model.getInstance().getGame());

        nL.withNeighbors(alicePlatform, nT, nM, nB);
        nR.withNeighbors(bobPlatform, nT, nM, nB);
        nM.withNeighbors(nT, nB);

        Parent alicePlayerCardParent = GameScreenBuilder.buildPlayerCardVBox(alice);
        Parent bobPlayerCardParent = GameScreenBuilder.buildPlayerCardVBox(bob);

        Parent alicePlatformParent = GameScreenBuilder.buildPlatformStackPane(alicePlatform);
        Parent bobPlatformParent = GameScreenBuilder.buildPlatformStackPane(bobPlatform);
        Parent nLParent = GameScreenBuilder.buildPlatformStackPane(nL);
        Parent nRParent = GameScreenBuilder.buildPlatformStackPane(nR);
        Parent nTParent = GameScreenBuilder.buildPlatformStackPane(nT);
        Parent nMParent = GameScreenBuilder.buildPlatformStackPane(nM);
        Parent nBParent = GameScreenBuilder.buildPlatformStackPane(nB);

        VBox middlePlatformBox = new VBox(10, nTParent, nMParent, nBParent);
        middlePlatformBox.setAlignment(Pos.CENTER);

        HBox platformBox = new HBox(10, alicePlatformParent, nLParent, middlePlatformBox, nRParent, bobPlatformParent);
        platformBox.setAlignment(Pos.CENTER);

        VBox playerCardBox = new VBox(10, alicePlayerCardParent, bobPlayerCardParent);
        playerCardBox.setAlignment(Pos.TOP_LEFT);

        HBox mainBox = new HBox(20, playerCardBox, platformBox);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setStyle("-fx-background-color: #333333");

        Scene scene = new Scene(mainBox);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> gc.stopGameLoop());
        primaryStage.show();

        gc.startGameLoop();
    }
}
