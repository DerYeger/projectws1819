package de.uniks.liverisk.view;

import de.uniks.liverisk.controller.PlatformController;
import de.uniks.liverisk.controller.PlatformMeepleController;
import de.uniks.liverisk.controller.PlayerCardController;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameScreenBuilder {

    private static final int GAME_SCREEN_WIDTH = 800;
    private static final int GAME_SCREEN_HEIGHT = 600;

    public static Parent getGameScreen(final Stage stage) throws IOException {
        return buildGameScreenAnchorPane(stage);
    }

    public static AnchorPane buildGameScreenAnchorPane(final Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameScreenBuilder.class.getResource("gameScreen.fxml"));
        stage.setWidth(GAME_SCREEN_WIDTH);
        stage.setHeight(GAME_SCREEN_HEIGHT);
        return fxmlLoader.load();
    }

    public static StackPane buildPlatformStackPane(final Platform platform) throws IOException {
        Objects.requireNonNull(platform);

        FXMLLoader fxmlLoader = new FXMLLoader(GameScreenBuilder.class.getResource("platform.fxml"));
        StackPane platformStackPane = fxmlLoader.load();
        PlatformController controller = fxmlLoader.getController();
        controller.setPlatform(platform);
        return platformStackPane;
    }

    public static VBox buildPlayerCardVBox(final Player player) throws IOException {
        Objects.requireNonNull(player);

        FXMLLoader fxmlLoader = new FXMLLoader(GameScreenBuilder.class.getResource("playerCard.fxml"));
        VBox playerCardVBox = fxmlLoader.load();
        PlayerCardController controller = fxmlLoader.getController();
        controller.setPlayer(player);
        return playerCardVBox;
    }

    public static AnchorPane buildPlatformMeepleAnchorPane(final Platform platform, final int slot) throws IOException {
        Objects.requireNonNull(platform);

        FXMLLoader fxmlLoader = new FXMLLoader(GameScreenBuilder.class.getResource("platformMeeple.fxml"));
        AnchorPane platformMeepleAnchorPane = fxmlLoader.load();
        PlatformMeepleController controller = fxmlLoader.getController();
        controller.initialize(platform, slot);
        return  platformMeepleAnchorPane;
    }
}
