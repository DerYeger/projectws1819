package de.uniks.liverisk.view;

import de.uniks.liverisk.controller.*;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewBuilder {

    private static final int GAME_SCREEN_WIDTH = 800;
    private static final int GAME_SCREEN_HEIGHT = 600;

    public static VBox buildStartScreenVBox(final Stage stage) {
        Objects.requireNonNull(stage);

        Button twoPlayerButton = new Button("Start 2-player Game");
        Button threePlayerButton = new Button("Start 3-player Game");
        Button fourPlayerButton = new Button("Start 4-player Game");

        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        Label infoLabel = new Label("Please select the number of players for this round.");

        VBox startScreenVBox = new VBox(40);
        VBox labelBox = new VBox(10);
        VBox buttonBox = new VBox(20);

        new StartScreenController().initialize(stage, twoPlayerButton, threePlayerButton, fourPlayerButton);

        welcomeLabel.setStyle("-fx-font-size: 40");
        infoLabel.setStyle("-fx-font-size: 15");

        startScreenVBox.setAlignment(Pos.TOP_CENTER);
        startScreenVBox.setStyle("-fx-padding: 40 0 0 0");
        labelBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);

        startScreenVBox.getChildren().addAll(labelBox, buttonBox);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);
        buttonBox.getChildren().addAll(twoPlayerButton, threePlayerButton, fourPlayerButton);

        return startScreenVBox;
    }

    public static VBox buildPlayerEditorScreenVBox(final Stage stage) {
        Objects.requireNonNull(stage);

        Button startButton = new Button("Start");

        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        Label infoLabel = new Label("Please choose player names and colors.");

        VBox playerEditorScreenVBox = new VBox(40);
        VBox labelBox = new VBox(10);
        VBox editBox = new VBox(20);

        new PlayerEditorScreenController().initialize(stage, editBox, startButton);

        startButton.setStyle("-fx-pref-width: 140");

        welcomeLabel.setStyle("-fx-font-size: 40");
        infoLabel.setStyle("-fx-font-size: 15");

        playerEditorScreenVBox.setAlignment(Pos.TOP_CENTER);
        playerEditorScreenVBox.setStyle("-fx-padding: 40 0 0 0");
        labelBox.setAlignment(Pos.CENTER);
        editBox.setAlignment(Pos.CENTER);

        playerEditorScreenVBox.getChildren().addAll(labelBox, editBox, startButton);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);

        return playerEditorScreenVBox;
    }

    public static HBox buildPlayerEditorHBox(final Player player) {
        Objects.requireNonNull(player);

        HBox playerEditorHBox = new HBox(60);
        TextField nameField = new TextField();
        ColorPicker colorPicker = new ColorPicker();

        new PlayerEditHBoxController().setPlayer(player, nameField, colorPicker);

        colorPicker.getStyleClass().add("button");

        playerEditorHBox.setAlignment(Pos.CENTER);
        playerEditorHBox.setStyle("-fx-padding: 0 40 0 40");

        playerEditorHBox.getChildren().addAll(nameField, colorPicker);

        return playerEditorHBox;
    }

    public static AnchorPane buildGameScreenAnchorPane(final Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewBuilder.class.getResource("gameScreen.fxml"));
        stage.setWidth(GAME_SCREEN_WIDTH);
        stage.setHeight(GAME_SCREEN_HEIGHT);
        return fxmlLoader.load();
    }

    public static StackPane buildPlatformStackPane(final Platform platform) throws IOException {
        Objects.requireNonNull(platform);

        FXMLLoader fxmlLoader = new FXMLLoader(ViewBuilder.class.getResource("platform.fxml"));
        StackPane platformStackPane = fxmlLoader.load();
        PlatformController controller = fxmlLoader.getController();
        controller.setPlatform(platform);
        return platformStackPane;
    }

    public static VBox buildPlayerCardVBox(final Player player) throws IOException {
        Objects.requireNonNull(player);

        FXMLLoader fxmlLoader = new FXMLLoader(ViewBuilder.class.getResource("playerCard.fxml"));
        VBox playerCardVBox = fxmlLoader.load();
        PlayerCardController controller = fxmlLoader.getController();
        controller.setPlayer(player);
        return playerCardVBox;
    }

    public static AnchorPane buildPlatformMeepleAnchorPane(final Platform platform, final int slot) throws IOException {
        Objects.requireNonNull(platform);

        FXMLLoader fxmlLoader = new FXMLLoader(ViewBuilder.class.getResource("platformMeeple.fxml"));
        AnchorPane platformMeepleAnchorPane = fxmlLoader.load();
        PlatformMeepleController controller = fxmlLoader.getController();
        controller.initialize(platform, slot);
        return  platformMeepleAnchorPane;
    }
}
