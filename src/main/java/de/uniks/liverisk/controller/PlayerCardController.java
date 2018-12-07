package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.model.Unit;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.Objects;

public class PlayerCardController {

    @FXML
    Label playerNameLabel;

    @FXML
    HBox meeples;

    private Player player;

    public void initialize() {
        for (Node node : meeples.getChildren()) node.setVisible(false);
    }

    public void setMeepleColor(final String color) {
        for (Node node : meeples.getChildren()) {
            node.setStyle("-fx-fill: #" + color);
        }
    }

    public int getSpareUnitCount() {
        HashSet<Unit> spareUnits = new HashSet<>();
        for (Unit unit : player.getUnits()) {
            if (unit.getPlatform() == null) spareUnits.add(unit);
        }
        return spareUnits.size();
    }

    public void updateVisibleMeeples() {
        int count = getSpareUnitCount();
        for (Node node : meeples.getChildren()) {
            node.setVisible(false);
        }
        for (int i = 0; i < count; i++) {
            meeples.getChildren().get(i).setVisible(true);
        }
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
