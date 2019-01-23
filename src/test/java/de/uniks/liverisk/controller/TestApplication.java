package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.view.PlatformBuilder;
import de.uniks.liverisk.view.PlayerCardBuilder;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
        gc.initGame(4, 4);

        Player arthur = Model.getInstance().getGame().getPlayers().get(0);
        Platform arthurPlatform =  arthur.getPlatforms().get(0);
        arthurPlatform.setXPos(0);
        arthurPlatform.setYPos(0);

        Player bill = Model.getInstance().getGame().getPlayers().get(1);
        Platform billPlatform =  bill.getPlatforms().get(0);
        billPlatform.setXPos(4);
        billPlatform.setYPos(0);

        Player charles = Model.getInstance().getGame().getPlayers().get(2);
        Platform charlesPlatform =  charles.getPlatforms().get(0);
        charlesPlatform.setXPos(0);
        charlesPlatform.setYPos(2);

        Player dutch = Model.getInstance().getGame().getPlayers().get(3);
        Platform dutchPlatform =  dutch.getPlatforms().get(0);
        dutchPlatform.setXPos(4);
        dutchPlatform.setYPos(2);

        Platform tL = new Platform();
        Platform tC = new Platform();
        Platform tR = new Platform();

        Platform cL = new Platform();
        Platform cC = new Platform();
        Platform cR = new Platform();

        Platform bL = new Platform();
        Platform bC = new Platform();
        Platform bR = new Platform();

        tL.setGame(Model.getInstance().getGame())
                .setCapacity(3)
                .setXPos(1)
                .setYPos(0);
        tC.setGame(Model.getInstance().getGame())
                .setCapacity(5)
                .setXPos(2)
                .setYPos(0);
        tR.setGame(Model.getInstance().getGame())
                .setCapacity(3)
                .setXPos(3)
                .setYPos(0);

        cL.setGame(Model.getInstance().getGame())
                .setCapacity(5)
                .setXPos(0)
                .setYPos(1);
        cC.setGame(Model.getInstance().getGame())
                .setCapacity(5)
                .setXPos(2)
                .setYPos(1);
        cR.setGame(Model.getInstance().getGame())
                .setCapacity(5)
                .setXPos(4)
                .setYPos(1);

        bL.setGame(Model.getInstance().getGame())
                .setCapacity(3)
                .setXPos(1)
                .setYPos(2);
        bC.setGame(Model.getInstance().getGame())
                .setCapacity(5)
                .setXPos(2)
                .setYPos(2);
        bR.setGame(Model.getInstance().getGame())
                .setCapacity(3)
                .setXPos(3)
                .setYPos(2);

        arthurPlatform.withNeighbors(cL, tL);
        billPlatform.withNeighbors(cR, tR);
        charlesPlatform.withNeighbors(cL, bL);
        dutchPlatform.withNeighbors(cR, bR);

        tC.withNeighbors(tL, tR);
        bC.withNeighbors(bL, bR);

        cC.withNeighbors(tC, bC);

        Parent arthurPlayerCardParent = PlayerCardBuilder.buildPlayerCardVBox(arthur);
        Parent billPlayerCardParent = PlayerCardBuilder.buildPlayerCardVBox(bill);
        Parent charlesPlayerCardParent = PlayerCardBuilder.buildPlayerCardVBox(charles);
        Parent dutchPlayerCardParent = PlayerCardBuilder.buildPlayerCardVBox(dutch);

        Parent arthurPlatformParent = PlatformBuilder.buildPlatformStackPane(arthurPlatform);
        Parent billPlatformParent = PlatformBuilder.buildPlatformStackPane(billPlatform);
        Parent charlesPlatformParent = PlatformBuilder.buildPlatformStackPane(charlesPlatform);
        Parent dutchPlatformParent = PlatformBuilder.buildPlatformStackPane(dutchPlatform);

        Parent tLParent = PlatformBuilder.buildPlatformStackPane(tL);
        Parent tCParent = PlatformBuilder.buildPlatformStackPane(tC);
        Parent tRParent = PlatformBuilder.buildPlatformStackPane(tR);

        Parent cLParent = PlatformBuilder.buildPlatformStackPane(cL);
        Parent cCParent = PlatformBuilder.buildPlatformStackPane(cC);
        Parent cRParent = PlatformBuilder.buildPlatformStackPane(cR);

        Parent bLParent = PlatformBuilder.buildPlatformStackPane(bL);
        Parent bCParent = PlatformBuilder.buildPlatformStackPane(bC);
        Parent bRParent = PlatformBuilder.buildPlatformStackPane(bR);


        VBox playerCardBox = new VBox(10, arthurPlayerCardParent, billPlayerCardParent, charlesPlayerCardParent, dutchPlayerCardParent);
        playerCardBox.setPadding(new Insets(10, 10, 10, 10));
        playerCardBox.setAlignment(Pos.TOP_LEFT);
        playerCardBox.setLayoutX(600);
        playerCardBox.setLayoutY(0);

        GridPane platformGrid = new GridPane();
        platformGrid.setPadding(new Insets(10, 10, 10, 10));
        platformGrid.setHgap(10);
        platformGrid.setVgap(10);

        platformGrid.add(arthurPlatformParent, (int) arthurPlatform.getXPos(), (int) arthurPlatform.getYPos());
        platformGrid.add(billPlatformParent, (int) billPlatform.getXPos(), (int) billPlatform.getYPos());
        platformGrid.add(charlesPlatformParent, (int) charlesPlatform.getXPos(), (int) charlesPlatform.getYPos());
        platformGrid.add(dutchPlatformParent, (int) dutchPlatform.getXPos(), (int) dutchPlatform.getYPos());

        platformGrid.add(tLParent, (int) tL.getXPos(), (int) tL.getYPos());
        platformGrid.add(tCParent, (int) tC.getXPos(), (int) tC.getYPos());
        platformGrid.add(tRParent, (int) tR.getXPos(), (int) tR.getYPos());

        platformGrid.add(cLParent, (int) cL.getXPos(), (int) cL.getYPos());
        platformGrid.add(cCParent, (int) cC.getXPos(), (int) cC.getYPos());
        platformGrid.add(cRParent, (int) cR.getXPos(), (int) cR.getYPos());

        platformGrid.add(bLParent, (int) bL.getXPos(), (int) bL.getYPos());
        platformGrid.add(bCParent, (int) bC.getXPos(), (int) bC.getYPos());
        platformGrid.add(bRParent, (int) bR.getXPos(), (int) bR.getYPos());

        AnchorPane gameScreen = new AnchorPane(platformGrid, playerCardBox);
        gameScreen.setStyle("-fx-background-color: #333333");

        Scene scene = new Scene(gameScreen);

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> gc.stopGameLoop());
        primaryStage.show();

        gc.startGameLoop();
    }
}
