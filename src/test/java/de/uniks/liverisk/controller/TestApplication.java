package de.uniks.liverisk.controller;

import de.uniks.liverisk.view.GameScreenBuilder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //not part of the assignment, only used to test if implementation works
    @Override
    public void start(Stage primaryStage) throws Exception {
        GameController.getInstance().initGame(4, 4);

        Scene scene = new Scene(GameScreenBuilder.getGameScreen(primaryStage));

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> GameLoop.getInstance().stop());
        primaryStage.show();

        GameLoop.getInstance().start();
    }
}
