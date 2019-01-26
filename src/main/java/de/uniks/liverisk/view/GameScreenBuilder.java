package de.uniks.liverisk.view;

import de.uniks.liverisk.controller.GameScreenController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameScreenBuilder {

    public static Parent getGameScreen(final Stage stage) throws IOException {
        return buildGameScreenAnchorPane(stage);
    }

    private static AnchorPane buildGameScreenAnchorPane(final Stage stage) throws IOException {
        Objects.requireNonNull(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(GameScreenBuilder.class.getResource("gameScreen.fxml"));
        stage.setWidth(GameScreenController.GAME_SCREEN_WIDTH);
        stage.setHeight(GameScreenController.GAME_SCREEN_HEIGHT);
        return fxmlLoader.load();
    }


}
