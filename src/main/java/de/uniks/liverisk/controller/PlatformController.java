package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Game;
import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.view.PlatformBuilder;

import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.io.IOException;
import java.util.Objects;

public class PlatformController {

    private static final Color DEFAULT_PLATFORM_COLOR = Color.valueOf("#8d8d8d");

    @FXML
    private Ellipse platformShape;

    @FXML
    private HBox meepleBox;

    private Platform platform;

    public void setPlatform(final Platform platform) throws IOException {
        Objects.requireNonNull(platform);
        this.platform = platform;

        addPlatformMeeples();
        addListeners();

        updatePlatformColor();

        addHandlers();
    }

    private void addPlatformMeeples() throws IOException {
        for (int i = 1; i <= platform.getCapacity(); i++) {
            meepleBox.getChildren().add(PlatformBuilder.buildPlatformMeepleAnchorPane(platform, i));
        }
    }

    private void addListeners() {
        Model.getInstance().getGame().addPropertyChangeListener(Game.PROPERTY_selectedPlatform, evt -> {
            Platform selectedPlatform = Model.getInstance().getGame().getSelectedPlatform();
            platformShape.setStrokeWidth((selectedPlatform != null && selectedPlatform.equals(platform)) ? 5 : 0);
        });
        platform.addPropertyChangeListener(Platform.PROPERTY_player, evt -> {
            updatePlatformColor();
            Game game = Model.getInstance().getGame();
            if (game.getSelectedPlatform() != null && game.getSelectedPlatform().equals(platform)) game.setSelectedPlatform(null);
        });
    }

    private void addHandlers() {
        meepleBox.setOnMouseClicked(this::onMouseClicked);
    }

    private synchronized void updatePlatformColor() {
        Player player = platform.getPlayer();
        if (player != null) {
            platformShape.setFill(Color.valueOf(player.getColor()));
        } else {
            platformShape.setFill(DEFAULT_PLATFORM_COLOR);
        }
    }

    private void onMouseClicked(MouseEvent e) {
        Objects.requireNonNull(e);

        if (e.getButton().equals(MouseButton.PRIMARY)) {
            leftClick();
        } else if (e.getButton().equals(MouseButton.SECONDARY)) {
            rightClick();
        }
    }

    private void leftClick() {
        Game game = Model.getInstance().getGame();
        Platform selectedPlatform = game.getSelectedPlatform();

        if (selectedPlatform == null && platform.getPlayer() == game.getCurrentPlayer()) { //select
            game.setSelectedPlatform(platform);
        } else if (selectedPlatform != null && selectedPlatform.equals(platform)) { //unselect
            clearSelection();
        } else if (selectedPlatform != null) { //action
            moveOrAttack(selectedPlatform, platform);
        }
    }

    private void moveOrAttack(final Platform source, final Platform destination) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(destination);

        GameController gameController = GameController.getInstance();

        if (destination.getPlayer() == null || destination.getPlayer().equals(source.getPlayer())) {
            if (gameController.concurrentMove(source, destination)) clearSelection();
        } else {
            if (gameController.concurrentAttack(source, destination)) clearSelection();
        }
    }

    private void clearSelection() {
        Model.getInstance().getGame().setSelectedPlatform(null);
    }

    private void rightClick() {
        Player currentPlayer = Model.getInstance().getGame().getCurrentPlayer();
        Player player = platform.getPlayer();
        if (player != null && player.equals(currentPlayer)) GameController.getInstance().reenforce(platform);
    }
}
