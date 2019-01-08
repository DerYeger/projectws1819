package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Game;
import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.view.SceneBuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GameScreenController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox playerList;

    public void initialize() {
        Game game = Model.getInstance().getGame();
        for (Player player : game.getPlayers()) {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneBuilder.class.getResource("playerCard.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                PlayerCardController controller = fxmlLoader.getController();
                controller.setPlayer(player);
                playerList.getChildren().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //PLACEHOLDER, just for visualisation
        VBox vBox = new VBox(20);
        for (int i = 0; i < Model.getInstance().getGame().getPlayers().size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneBuilder.class.getResource("platform.fxml"));
            try {
                Parent platform = fxmlLoader.load();
                PlatformController controller = fxmlLoader.getController();
                controller.setPlatform(game.getPlayers().get(i).getPlatforms().get(0));
                vBox.getChildren().add(platform);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        anchorPane.getChildren().add(vBox);
        //PLACEHOLDER END
    }
}
