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
import javafx.scene.shape.Line;

import java.io.IOException;

public class GameScreenController {

    public static final int GAME_SCREEN_WIDTH = 1025;
    public static final int GAME_SCREEN_HEIGHT = 800;

    @FXML
    private Pane platformAreaPane;

    @FXML
    private VBox playerList;

    public void initialize() throws IOException {
        showPlayerCards();

        drawPlatformConnections();
        drawPlatforms();

        GameController.getInstance().startGameLoop();
    }

    private void showPlayerCards() throws IOException {
        Game game = Model.getInstance().getGame();
        for (Player player : game.getPlayers()) {
            playerList.getChildren().add(PlayerCardBuilder.buildPlayerCardVBox(player));
        }
    }

    private void drawPlatforms() throws IOException {
        for (Platform platform : Model.getInstance().getGame().getPlatforms()) {
            Parent platformParent = PlatformBuilder.buildPlatformStackPane(platform);
            platformParent.setLayoutX(platform.getXPos());
            platformParent.setLayoutY(platform.getYPos());
            platformAreaPane.getChildren().add(platformParent);
        }
    }

    private void drawPlatformConnections() {
        for (Platform platform : Model.getInstance().getGame().getPlatforms()) {
            for (Platform neighbor : platform.getNeighbors()) {
                double startXPos = platform.getXPos() + PlatformController.PLATFORM_WIDTH / 2.0;
                double startYPos = platform.getYPos() + PlatformController.PLATFORM_HEIGHT / 2.0;
                double endXPos = neighbor.getXPos() + PlatformController.PLATFORM_WIDTH / 2.0;
                double endYPos = neighbor.getYPos() + PlatformController.PLATFORM_HEIGHT / 2.0;
                Line connection = new Line(startXPos, startYPos, endXPos, endYPos);
                connection.setStrokeWidth(5);
                platformAreaPane.getChildren().add(connection);
            }
        }
    }
}
