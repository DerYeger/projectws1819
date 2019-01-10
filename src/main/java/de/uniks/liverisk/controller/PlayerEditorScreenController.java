package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.view.ViewBuilder;

import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Objects;

import static de.uniks.liverisk.util.ThrowingEventHandlerWrapper.throwingEventHandlerWrapper;

public class PlayerEditorScreenController {

    private Stage stage;

    private Button startButton;

    public void initialize(final Stage stage, final Button startButton) {
        Objects.requireNonNull(stage);
        Objects.requireNonNull(startButton);
        this.stage = stage;
        this.startButton = startButton;
        addListeners();
        addHandlers();
    }

    private void addListeners() {
        Model.getInstance().getGame().getPlayers().forEach(player -> {
            player.addPropertyChangeListener(Player.PROPERTY_name,
                    evt -> updateStartButtonUsability());
            player.addPropertyChangeListener(Player.PROPERTY_color,
                    evt -> updateStartButtonUsability());
        });
    }

    private void addHandlers() {
        startButton.setOnAction(throwingEventHandlerWrapper(event -> {
            stage.getScene().setRoot(ViewBuilder.buildGameScreenAnchorPane(stage));
        }));
    }

    private void updateStartButtonUsability() {
        startButton.setDisable(!new GameController().playerConfigurationIsValid());
    }
}
