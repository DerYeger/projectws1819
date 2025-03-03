package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Platform;

import javafx.fxml.FXML;
import javafx.scene.shape.Polygon;

import java.util.Objects;

public class PlatformMeepleController {

    @FXML
    private Polygon meeple;

    private Platform platform;

    private int slot;

    public void initialize(final Platform platform, final int slot) {
        Objects.requireNonNull(platform);

        this.platform = platform;
        this.slot = slot;

        addListeners();
        updateMeeple();
    }

    private void addListeners() {
        platform.addPropertyChangeListener(Platform.PROPERTY_units, evt -> javafx.application.Platform.runLater(this::updateMeeple));
    }

    private void updateMeeple() {
        meeple.setVisible(platform.getUnits().size() >= slot);
    }
}
