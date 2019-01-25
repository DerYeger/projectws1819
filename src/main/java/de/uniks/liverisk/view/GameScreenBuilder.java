package de.uniks.liverisk.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameScreenBuilder {

    public static final int GAME_SCREEN_WIDTH = 1025;
    public static final int GAME_SCREEN_HEIGHT = 800;

    public static Parent getGameScreen(final Stage stage) throws IOException {
        return buildGameScreenAnchorPane(stage);
    }

    private static AnchorPane buildGameScreenAnchorPane(final Stage stage) throws IOException {
        Objects.requireNonNull(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(GameScreenBuilder.class.getResource("gameScreen.fxml"));
        stage.setWidth(GAME_SCREEN_WIDTH);
        stage.setHeight(GAME_SCREEN_HEIGHT);
        return fxmlLoader.load();
    }


}
