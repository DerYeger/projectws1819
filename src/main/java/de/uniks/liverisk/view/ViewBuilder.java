package de.uniks.liverisk.view;

import de.uniks.liverisk.controller.*;
import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static de.uniks.liverisk.util.ThrowingEventHandlerWrapper.throwingEventHandlerWrapper;

public class ViewBuilder {

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

        welcomeLabel.setStyle("-fx-font-size: 40");
        infoLabel.setStyle("-fx-font-size: 15");

        startScreenVBox.setAlignment(Pos.TOP_CENTER);
        startScreenVBox.setStyle("-fx-padding: 40 0 0 0");
        labelBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);

        startScreenVBox.getChildren().addAll(labelBox, buttonBox);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);
        buttonBox.getChildren().addAll(twoPlayerButton, threePlayerButton, fourPlayerButton);

        new StartScreenController().initialize(stage, twoPlayerButton, threePlayerButton, fourPlayerButton);

        return startScreenVBox;
    }

    public static VBox buildPlayerEditorScreenVBox(final Stage stage, final int playerCount) {
        Objects.requireNonNull(stage);

        new GameController().initGame(playerCount);

        Button startButton = new Button("Start");

        Label welcomeLabel = new Label("Welcome to LiveRisk.");
        Label infoLabel = new Label("Please choose player names and colors.");

        VBox playerEditorScreenVBox = new VBox(40);
        VBox labelBox = new VBox(10);
        VBox editBox = new VBox(20);

        startButton.setStyle("-fx-pref-width: 140");

        welcomeLabel.setStyle("-fx-font-size: 40");
        infoLabel.setStyle("-fx-font-size: 15");

        playerEditorScreenVBox.setAlignment(Pos.TOP_CENTER);
        playerEditorScreenVBox.setStyle("-fx-padding: 40 0 0 0");
        labelBox.setAlignment(Pos.CENTER);
        editBox.setAlignment(Pos.CENTER);

        startButton.setOnAction(throwingEventHandlerWrapper(event -> {
            stage.getScene().setRoot(buildGameScreenAnchorPane());
            stage.setWidth(800);
            stage.setHeight(600);
        }));

        Model.getInstance().getGame().getPlayers().forEach(player -> editBox.getChildren().add(buildPlayerEditorHBox(player)));

        playerEditorScreenVBox.getChildren().addAll(labelBox, editBox, startButton);
        labelBox.getChildren().addAll(welcomeLabel, infoLabel);

        new PlayerEditorScreenController().initialize(startButton);

        return playerEditorScreenVBox;
    }

    private static HBox buildPlayerEditorHBox(final Player player) {
        Objects.requireNonNull(player);

        HBox playerEditorHBox = new HBox(60);
        TextField nameField = new TextField(player.getName());
        ColorPicker colorPicker = new ColorPicker(Color.web(player.getColor()));

        colorPicker.getStyleClass().add("button");
        String color = colorPicker.getValue().toString();
        color = color.substring(2, color.length() - 2);
        colorPicker.setStyle("-fx-background-color: #" + color);

        playerEditorHBox.setAlignment(Pos.CENTER);
        playerEditorHBox.setStyle("-fx-padding: 0 40 0 40");

        playerEditorHBox.getChildren().addAll(nameField, colorPicker);

        new PlayerEditHBoxController().initialize(player, nameField, colorPicker);

        return playerEditorHBox;
    }

    private static AnchorPane buildGameScreenAnchorPane() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewBuilder.class.getResource("gameScreen.fxml"));
        return fxmlLoader.load();
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
