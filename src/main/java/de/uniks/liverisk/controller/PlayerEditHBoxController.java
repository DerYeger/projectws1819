package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Player;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import java.util.Objects;


public class PlayerEditHBoxController {

    public void initialize(final Player player, final TextField nameField, final ColorPicker colorPicker) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(nameField);
        Objects.requireNonNull(colorPicker);

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            player.setName(newValue);
        });
        colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            player.setColor(newValue.toString());
            String newColor = newValue.toString().substring(2, newValue.toString().length() - 2);
            colorPicker.setStyle("-fx-background-color: #" + newColor);
        });
    }
}
