package de.uniks.liverisk.controller;

import de.uniks.liverisk.view.ViewBuilder;

import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Objects;

import static de.uniks.liverisk.util.ThrowingEventHandlerWrapper.throwingEventHandlerWrapper;


public class StartScreenController {

    public void initialize(final Stage stage, final Button... buttons) {
        Objects.requireNonNull(stage);
        Objects.requireNonNull(buttons);

        for (int i = 0; i < buttons.length; i++) {
            final int playerCount = i + 2;
            buttons[i].setOnAction(throwingEventHandlerWrapper(e -> {
                new GameController().initGame(playerCount);
                stage.getScene().setRoot(ViewBuilder.buildPlayerEditorScreenVBox(stage));
            }));
        }
    }
}
