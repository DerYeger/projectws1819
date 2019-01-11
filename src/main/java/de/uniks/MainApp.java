package de.uniks;

import de.uniks.liverisk.view.ViewBuilder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    //main method required by assignment
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox startSceneVBox = ViewBuilder.buildStartScreenVBox(primaryStage);

        Scene scene = new Scene(startSceneVBox,600, 570);
        scene.getStylesheets().add(ViewBuilder.class.getResource("main.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
