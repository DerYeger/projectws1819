package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.view.PlayerCardBuilder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class PlayerCardController {

    @FXML
    private Label playerNameLabel;

    @FXML
    private HBox meeples;

    private Player player;

    public void setPlayer(final Player player) {
        Objects.requireNonNull(player);
        this.player = player;

        addPlayerCardMeeples();
        setPlayerNameLabelText();
    }

    private void addPlayerCardMeeples() {
        for (int i = 1; i <= GameController.MAX_SPARE_UNIT_COUNT; i++) {
            meeples.getChildren().add(PlayerCardBuilder.buildPlayerCardMeepleCircle(player, i));
        }
    }

    private void setPlayerNameLabelText() {
        playerNameLabel.setText(player.getName());
    }
}
