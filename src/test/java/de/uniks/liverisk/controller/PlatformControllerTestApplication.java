package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.model.Unit;
import de.uniks.liverisk.view.SceneBuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PlatformControllerTestApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //not part of the assignment, only used to test if implementation works
    @Override
    public void start(Stage primaryStage) {
        GameController gc = new GameController();
        gc.initGame(2);

        Player player = Model.getInstance().getGame().getPlayers().get(0);
        Platform platform =  player.getPlatforms().get(0);


        FXMLLoader platformLoader = new FXMLLoader(SceneBuilder.class.getResource("platform.fxml"));
        FXMLLoader playerCardLoader = new FXMLLoader(SceneBuilder.class.getResource("playerCard.fxml"));

        try {
            Parent platformParent = platformLoader.load();
            PlatformController platformController = platformLoader.getController();
            platformController.setPlatform(platform);

            Parent playerCardParent = playerCardLoader.load();
            PlayerCardController playerCardController = playerCardLoader.getController();
            playerCardController.setPlayer(Model.getInstance().getGame().getPlayers().get(0));

            Button reenforceButton = new Button("Reenforce platform");
            Button addSpareUnitButton = new Button("Add new spare Unit");
            Button clearPlatformButton = new Button("Remove unit from platform");

            reenforceButton.setOnAction(e -> gc.reenforce(platform));
            addSpareUnitButton.setOnAction(e -> player.withUnits(new Unit()));
            clearPlatformButton.setOnAction(e -> platform.getUnits().get(0).removeYou());

            VBox buttonBox = new VBox(5, reenforceButton, addSpareUnitButton, clearPlatformButton);
            VBox parentBox = new VBox(10, platformParent, playerCardParent);
            HBox mainBox = new HBox(20, parentBox, buttonBox);
            Scene scene = new Scene(mainBox, 500, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
