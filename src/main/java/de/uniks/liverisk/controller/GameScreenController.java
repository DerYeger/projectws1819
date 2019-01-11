package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Game;
import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.view.ViewBuilder;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GameScreenController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox playerList;

    public void initialize() throws IOException {
        Game game = Model.getInstance().getGame();
        for (Player player : game.getPlayers()) {
            playerList.getChildren().add(ViewBuilder.buildPlayerCardVBox(player));
        }

        //
        //PLACEHOLDER, just for visualisation
        VBox vBox = new VBox(20);
        for (Player player : game.getPlayers()) {
            Platform platform = player.getPlatforms().get(0);
            vBox.getChildren().add(ViewBuilder.buildPlatformStackPane(platform));
        }
        anchorPane.getChildren().add(vBox);
        //PLACEHOLDER END
        //
    }
}
