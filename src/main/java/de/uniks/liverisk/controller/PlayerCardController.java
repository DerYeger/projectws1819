package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.model.Unit;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PlayerCardController {

    @FXML
    Label playerNameLabel;

    @FXML
    HBox meeples;

    public void initialize() {
        for (Node node : meeples.getChildren()) {
            node.setVisible(false);
        }
    }

    public void setPlayer(Player player) {
        playerNameLabel.setText(player.getName());
        String color = player.getColor();
        color = color.substring(2, color.length() - 2);
        for (Node node : meeples.getChildren()) {
            node.setStyle("-fx-fill: #" + color);
        }
        for (int i = 0; i < player.getUnits().size(); i++) {
            meeples.getChildren().get(i).setVisible(true);
        }
    }

}
