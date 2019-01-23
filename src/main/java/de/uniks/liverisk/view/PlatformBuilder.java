package de.uniks.liverisk.view;

import de.uniks.liverisk.controller.PlatformController;
import de.uniks.liverisk.controller.PlatformMeepleController;
import de.uniks.liverisk.model.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class PlatformBuilder {

    public static StackPane buildPlatformStackPane(final Platform platform) throws IOException {
        Objects.requireNonNull(platform);

        FXMLLoader fxmlLoader = new FXMLLoader(GameScreenBuilder.class.getResource("platform.fxml"));
        StackPane platformStackPane = fxmlLoader.load();
        PlatformController controller = fxmlLoader.getController();
        controller.setPlatform(platform);
        return platformStackPane;
    }

    public static AnchorPane buildPlatformMeepleAnchorPane(final Platform platform, final int slot) throws IOException {
        Objects.requireNonNull(platform);

        FXMLLoader fxmlLoader = new FXMLLoader(GameScreenBuilder.class.getResource("platformMeeple.fxml"));
        AnchorPane platformMeepleAnchorPane = fxmlLoader.load();
        PlatformMeepleController controller = fxmlLoader.getController();
        controller.initialize(platform, slot);
        return  platformMeepleAnchorPane;
    }
}
