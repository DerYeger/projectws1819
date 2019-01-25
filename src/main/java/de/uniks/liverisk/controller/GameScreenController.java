package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Game;
import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.view.PlatformBuilder;
import de.uniks.liverisk.view.PlayerCardBuilder;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GameScreenController {

    @FXML
    private Pane platformAreaPane;

    @FXML
    private VBox playerList;

    public void initialize() throws IOException {
        showPlayerCards();

        showPlatforms();

        GameController.getInstance().startGameLoop();
    }

    private void showPlayerCards() throws IOException {
        Game game = Model.getInstance().getGame();
        for (Player player : game.getPlayers()) {
            playerList.getChildren().add(PlayerCardBuilder.buildPlayerCardVBox(player));
        }
    }

    private void showPlatforms() throws IOException {
        for (Platform platform : Model.getInstance().getGame().getPlatforms()) {
            Parent platformParent = PlatformBuilder.buildPlatformStackPane(platform);
            platformParent.setLayoutX(platform.getXPos());
            platformParent.setLayoutY(platform.getYPos());
            platformAreaPane.getChildren().add(platformParent);
        }
    }
}
