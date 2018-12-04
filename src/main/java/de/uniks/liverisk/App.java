package de.uniks.liverisk;

import de.uniks.liverisk.controller.GameController;
import de.uniks.liverisk.view.SceneBuilder;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    private GameController gc;

    //main method required by assignment
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gc = new GameController();
        primaryStage.setScene(SceneBuilder.buildStartScene(primaryStage, gc));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


}
