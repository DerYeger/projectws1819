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
import java.util.ArrayList;

public class PlayerCardControllerTestApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //not part of the assignment, only used to test if implementation works
    @Override
    public void start(Stage primaryStage) {
        GameController gc = new GameController();
        gc.initGame(2);

        Player player = Model.getInstance().getGame().getPlayers().get(0);
        Platform platform = new Platform();
        platform.setCapacity(5).setPlayer(player).withNeighbors(player.getPlatforms().get(0));

        FXMLLoader fxmlLoader = new FXMLLoader(SceneBuilder.class.getResource("playerCard.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            PlayerCardController controller = fxmlLoader.getController();
            controller.setPlayer(player);

            Button reenforceButton = new Button("Reenforce platform");
            Button addSpareUnitButton = new Button("Add new spare Unit");
            Button removeUnitButton = new Button("Remove spare unit");

            reenforceButton.setOnAction(e -> gc.reenforce(platform));
            addSpareUnitButton.setOnAction(e -> player.withUnits(new Unit()));
            removeUnitButton.setOnAction(e -> new ArrayList<Unit>(player.getUnits()).stream().filter(u -> u.getPlatform() == null).limit(1).forEach(Unit::removeYou));

            VBox vbox = new VBox(5, reenforceButton, addSpareUnitButton, removeUnitButton);
            HBox hbox = new HBox(20, vbox, parent);
            Scene scene = new Scene(hbox, 350, 100);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
