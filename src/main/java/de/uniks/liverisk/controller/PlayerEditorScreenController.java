package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Player;

import javafx.scene.control.Button;

import java.util.Objects;

public class PlayerEditorScreenController {

    private Button startButton;

    public void initialize(final Button startButton) {
        Objects.requireNonNull(startButton);
        this.startButton = startButton;

        Model.getInstance().getGame().getPlayers().forEach(player -> {
            player.addPropertyChangeListener(Player.PROPERTY_name,
                    evt -> updateStartButtonUsability());
            player.addPropertyChangeListener(Player.PROPERTY_color,
                    evt -> updateStartButtonUsability());
        });
    }

    private void updateStartButtonUsability() {
        startButton.setDisable(!new GameController().playerConfigurationIsValid());
    }
}
