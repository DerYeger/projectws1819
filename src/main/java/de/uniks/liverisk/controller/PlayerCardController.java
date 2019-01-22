package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.util.Utils;

import javafx.fxml.FXML;
import javafx.scene.Node;
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

        addListeners();

        playerNameLabel.setText(player.getName());
        setMeepleColor(player.getColor());
        updateVisibleMeeples();
    }

    private void addListeners() {
        player.addPropertyChangeListener(Player.PROPERTY_units, evt -> updateVisibleMeeples());
    }

    private void setMeepleColor(final String color) {
        for (Node node : meeples.getChildren()) {
            node.setStyle("-fx-fill: " + Utils.hexColorStringToWebColorString(color));
        }
    }

    //TODO fix indexoutofbounds error
    //proposal: implement playercardmeeplecontroller and change visibilty similar to platformmeeplecontroller
     private void updateVisibleMeeples() {
         int count = player.getUnits().size();
         for (int i = 0; i < meeples.getChildren().size(); i++) {
             meeples.getChildren().get(i).setVisible(i < count);
         }
    }
}
