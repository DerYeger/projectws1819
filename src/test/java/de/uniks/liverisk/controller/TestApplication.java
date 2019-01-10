package de.uniks.liverisk.controller;

import de.uniks.liverisk.model.Model;
import de.uniks.liverisk.model.Platform;
import de.uniks.liverisk.model.Player;
import de.uniks.liverisk.model.Unit;
import de.uniks.liverisk.view.ViewBuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class TestApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //not part of the assignment, only used to test if implementation works
    @Override
    public void start(Stage primaryStage) {
        GameController gc = new GameController();
        gc.initGame(2);

        Player alice = Model.getInstance().getGame().getPlayers().get(0);
        alice.setName("Alice");
        Platform alicePlatform =  alice.getPlatforms().get(0);

        Player bob = Model.getInstance().getGame().getPlayers().get(1);
        bob.setName("Bob");
        Platform bobPlatform =  bob.getPlatforms().get(0);

        Platform neutralPlatform = new Platform();
        neutralPlatform.setCapacity(3).setGame(Model.getInstance().getGame()).withNeighbors(alicePlatform, bobPlatform);

        FXMLLoader alicePlatformLoader = new FXMLLoader(ViewBuilder.class.getResource("platform.fxml"));
        FXMLLoader bobPlatformLoader = new FXMLLoader(ViewBuilder.class.getResource("platform.fxml"));
        FXMLLoader neutralPlatformLoader = new FXMLLoader(ViewBuilder.class.getResource("platform.fxml"));

        FXMLLoader alicePlayerCardLoader = new FXMLLoader(ViewBuilder.class.getResource("playerCard.fxml"));
        FXMLLoader bobPlayerCardLoader = new FXMLLoader(ViewBuilder.class.getResource("playerCard.fxml"));

        try {
            Parent alicePlatformParent = alicePlatformLoader.load();
            PlatformController alicePlatformController = alicePlatformLoader.getController();
            alicePlatformController.setPlatform(alicePlatform);

            Parent bobPlatformParent = bobPlatformLoader.load();
            PlatformController bobPlatformController = bobPlatformLoader.getController();
            bobPlatformController.setPlatform(bobPlatform);

            Parent neutralPlatformParent = neutralPlatformLoader.load();
            PlatformController neutralPlatformController = neutralPlatformLoader.getController();
            neutralPlatformController.setPlatform(neutralPlatform);

            Parent alicePlayerCardParent = alicePlayerCardLoader.load();
            PlayerCardController alicePlayerCardController = alicePlayerCardLoader.getController();
            alicePlayerCardController.setPlayer(alice);

            Parent bobPlayerCardParent = bobPlayerCardLoader.load();
            PlayerCardController bobPlayerCardController = bobPlayerCardLoader.getController();
            bobPlayerCardController.setPlayer(bob);

            Button aliceReenforceButton = new Button("Alice reenforce platform");
            Button aliceAddSpareUnitButton = new Button("Add spare unit to Alice");
            Button aliceAttackButton = new Button("Alice attacks neutral platform");
            Button aliceMoveButton = new Button("Alice moves to neutral platform");

            Button bobReenforceButton = new Button("Bob reenforces platform");
            Button bobAddSpareUnitButton = new Button("Add spare unit to Bob");
            Button bobAttackButton = new Button("Bob attacks neutral platform");
            Button bobMoveButton = new Button("Bob moves to neutral platform");

            aliceReenforceButton.setOnAction(e -> gc.reenforce(alicePlatform));
            aliceAddSpareUnitButton.setOnAction(e -> alice.withUnits(new Unit()));
            aliceAttackButton.setOnAction(e -> gc.attack(alicePlatform, neutralPlatform));
            aliceMoveButton.setOnAction(e -> gc.move(alicePlatform, neutralPlatform));

            bobReenforceButton.setOnAction(e -> gc.reenforce(bobPlatform));
            bobAddSpareUnitButton.setOnAction(e -> bob.withUnits(new Unit()));
            bobAttackButton.setOnAction(e -> gc.attack(bobPlatform, neutralPlatform));
            bobMoveButton.setOnAction(e -> gc.move(bobPlatform, neutralPlatform));

            alice.addPropertyChangeListener(Player.PROPERTY_units, evt -> {
                aliceReenforceButton.setDisable(canNotReenforce(alice));
                aliceAddSpareUnitButton.setDisable(canAddSpareUnit(alice));
            });
            bob.addPropertyChangeListener(Player.PROPERTY_units, evt -> {
                bobReenforceButton.setDisable(canNotReenforce(bob));
                bobAddSpareUnitButton.setDisable(canAddSpareUnit(bob));
            });

            alicePlatform.addPropertyChangeListener(Platform.PROPERTY_units, evt -> {
                aliceReenforceButton.setDisable(canNotReenforce(alice));
                aliceAddSpareUnitButton.setDisable(canAddSpareUnit(alice));
                aliceAttackButton.setDisable(canNotAttack(alice, neutralPlatform));
                aliceMoveButton.setDisable(canNotMove(alice, neutralPlatform));
            });
            bobPlatform.addPropertyChangeListener(Platform.PROPERTY_units, evt -> {
                bobReenforceButton.setDisable(canNotReenforce(bob));
                bobAddSpareUnitButton.setDisable(canAddSpareUnit(bob));
                bobAttackButton.setDisable(canNotAttack(bob, neutralPlatform));
                bobMoveButton.setDisable(canNotMove(bob, neutralPlatform));
            });
            neutralPlatform.addPropertyChangeListener(Platform.PROPERTY_player, evt -> {
                aliceReenforceButton.setDisable(canNotReenforce(alice));
                aliceAttackButton.setDisable(canNotAttack(alice, neutralPlatform));
                aliceMoveButton.setDisable(canNotMove(alice, neutralPlatform));
                bobReenforceButton.setDisable(canNotReenforce(bob));
                bobAttackButton.setDisable(canNotAttack(bob, neutralPlatform));
                bobMoveButton.setDisable(canNotMove(bob, neutralPlatform));
            });
            neutralPlatform.addPropertyChangeListener(Platform.PROPERTY_units, evt -> {
                aliceReenforceButton.setDisable(canNotReenforce(alice));
                aliceAttackButton.setDisable(canNotAttack(alice, neutralPlatform));
                aliceMoveButton.setDisable(canNotMove(alice, neutralPlatform));
                bobReenforceButton.setDisable(canNotReenforce(bob));
                bobAttackButton.setDisable(canNotAttack(bob, neutralPlatform));
                bobMoveButton.setDisable(canNotMove(bob, neutralPlatform));
            });

            neutralPlatform.firePropertyChange(Platform.PROPERTY_player, null , null);

            VBox aliceBox = new VBox(5, alicePlayerCardParent, aliceReenforceButton, aliceAddSpareUnitButton, aliceAttackButton, aliceMoveButton);
            VBox bobBox = new VBox(5, bobPlayerCardParent, bobReenforceButton, bobAddSpareUnitButton, bobAttackButton, bobMoveButton);
            HBox platformBox = new HBox(10, alicePlatformParent, neutralPlatformParent,bobPlatformParent);
            platformBox.setPadding(new Insets(60, 10, 10, 10));
            HBox mainBox = new HBox(20, aliceBox, platformBox, bobBox);
            mainBox.setAlignment(Pos.CENTER);
            mainBox.setStyle("-fx-background-color: #333333");
            Scene scene = new Scene(mainBox, 800, 200);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean canNotReenforce(Player player) {
        return player.getPlatforms().get(0).getUnits().size() == player.getPlatforms().get(0).getCapacity() || player.getUnits().stream().filter(u -> u.getPlatform() == null).count() < 1;
    }

    private static boolean canAddSpareUnit(Player player) {
        return player.getUnits().stream().filter(u -> u.getPlatform() == null).count() >= 8;
    }

    private static boolean canNotAttack(Player player, Platform platform) {
        return platform.getPlayer() == null || platform.getPlayer() == player || player.getPlatforms().get(0).getUnits().size() < 2;
    }

    private static boolean canNotMove(Player player, Platform platform) {
        return player.getPlatforms().get(0).getUnits().size() < 2 || platform.getPlayer() != null && platform.getPlayer() != player || platform.getPlayer() == player && platform.getUnits().size() == platform.getCapacity();
    }

}
