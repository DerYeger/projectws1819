package de.uniks;

import de.uniks.liverisk.controller.GameController;
import de.uniks.liverisk.view.StartScreenBuilder;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    //main method required by assignment
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = StartScreenBuilder.getStartScreen(primaryStage);

        Scene scene = new Scene(parent,600, 570);
        scene.getStylesheets().add(StartScreenBuilder.class.getResource("main.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(event -> GameController.getInstance().stopGameLoop());
        primaryStage.show();
    }

}
