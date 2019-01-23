package de.uniks.liverisk.view;

import de.uniks.liverisk.controller.PlayerCardController;
import de.uniks.liverisk.controller.PlayerCardMeepleController;
import de.uniks.liverisk.model.Player;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import java.io.IOException;
import java.util.Objects;

public class PlayerCardBuilder {

    public static VBox buildPlayerCardVBox(final Player player) throws IOException {
        Objects.requireNonNull(player);

        FXMLLoader fxmlLoader = new FXMLLoader(GameScreenBuilder.class.getResource("playerCard.fxml"));
        VBox playerCardVBox = fxmlLoader.load();
        PlayerCardController controller = fxmlLoader.getController();
        controller.setPlayer(player);
        return playerCardVBox;
    }

    public static Circle buildPlayerCardMeepleCircle(final Player player, final int slot) {
        Objects.requireNonNull(player);

        Circle circle = new Circle(5);
        circle.setStrokeType(StrokeType.INSIDE);
        new PlayerCardMeepleController().initialize(circle, player, slot);
        return circle;
    }
}
