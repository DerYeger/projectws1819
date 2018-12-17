package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Platform;
import javafx.fxml.FXML;
import javafx.scene.shape.Polygon;

public class PlatformMeepleController {

    @FXML
    Polygon meeple;

    int slot;
    Platform platform;

    public void init(final Platform platform, final int slot) {
        this.platform = platform;
        this.slot = slot;
        platform.addPropertyChangeListener(Platform.PROPERTY_units, evt -> updateMeeple());
        updateMeeple();
    }

    private void updateMeeple() {
        meeple.setVisible(platform.getUnits().size() > slot);
    }

}
