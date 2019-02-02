package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.view.GameScreenBuilder;
import de.uniks.liverisk.view.PlayerEditorScreenBuilder;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static de.uniks.liverisk.util.ThrowingEventHandlerWrapper.throwingEventHandlerWrapper;


public class StartScreenController {

    private Stage stage;

    public void initialize(final Stage stage, final Button loadButton, final Button... playerCountSelectionButtons) {
        Objects.requireNonNull(stage);
        Objects.requireNonNull(loadButton);
        Objects.requireNonNull(playerCountSelectionButtons);
        this.stage = stage;

        loadButton.setOnAction(throwingEventHandlerWrapper(event -> switchToGameScreen()));

        for (int i = 0; i < playerCountSelectionButtons.length; i++) {
            final int playerCount = i + 2;
            playerCountSelectionButtons[i].setOnAction(throwingEventHandlerWrapper(event -> switchToPlayerEditorScreen(playerCount)));
        }
    }

    private void switchToGameScreen() throws IOException {
        boolean loadingSuccessful = Model.getInstance().loadSavedGame();
        if (loadingSuccessful) {
            stage.getScene().setRoot(GameScreenBuilder.getGameScreen(stage));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to load saved game");
            alert.show();
        }
    }

    private void switchToPlayerEditorScreen(final int playerCount) throws Exception {
        GameController.getInstance().initGame(playerCount);
        stage.getScene().setRoot(PlayerEditorScreenBuilder.getPlayerEditorScreen(stage));
    }
}
