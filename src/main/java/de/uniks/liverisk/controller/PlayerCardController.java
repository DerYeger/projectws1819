package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.model.Unit;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class PlayerCardController {

    @FXML
    Label playerNameLabel;

    @FXML
    HBox meeples;

    private Player player;

    public void setMeepleColor(final String color) {
        for (Node node : meeples.getChildren()) {
            node.setStyle("-fx-fill: #" + color);
        }
    }

    public void updateVisibleMeeples() {
        int count = (int) player.getUnits().stream().filter(u -> u.getPlatform() == null).count();
        for (int i = 0; i < Math.min(count, meeples.getChildren().size()); i++) meeples.getChildren().get(i).setVisible(true);
        for (int i = count; i < meeples.getChildren().size(); i++) meeples.getChildren().get(i).setVisible(false);
    }

    public void setPlayer(final Player player) {
        Objects.requireNonNull(player);
        this.player = player;

        playerNameLabel.setText(player.getName());
        setMeepleColor(player.getColor().substring(2, player.getColor().length() - 2));

        updateVisibleMeeples();

        //adds property change listeners to player's units platform property
        for (Unit unit : player.getUnits()) {
            unit.addPropertyChangeListener("platform", pl -> {if (pl.getOldValue() == null) updateVisibleMeeples();});
        }

        //adds property change listener to the player's units property
        player.addPropertyChangeListener("units", ul -> {
            if (ul.getNewValue() != null) ((Unit) ul.getNewValue()).addPropertyChangeListener("platform", pl -> {if (pl.getOldValue() == null) updateVisibleMeeples();});
            updateVisibleMeeples();
        });
    }

}
