package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Player;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.Objects;

import static de.uniks.liverisk.util.Utils.hexColorStringToWebColorString;


public class PlayerEditHBoxController {

    private Player player;

    private TextField nameField;

    private ColorPicker colorPicker;

    public void setPlayer(final Player player, final TextField nameField, final ColorPicker colorPicker) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(nameField);
        Objects.requireNonNull(colorPicker);
        this.player = player;
        this.nameField = nameField;
        this.colorPicker = colorPicker;

        addListeners();

        nameField.setText(player.getName());
        colorPicker.setValue(Color.web(player.getColor()));
        setColorPickerBackGroundColor(player.getColor());
    }

    private void setColorPickerBackGroundColor(String color) {
        colorPicker.setStyle("-fx-background-color: " + hexColorStringToWebColorString(color));
    }

    private void addListeners() {
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            player.setName(newValue);
        });
        colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            player.setColor(newValue.toString());
            setColorPickerBackGroundColor(newValue.toString());
        });
    }
}
