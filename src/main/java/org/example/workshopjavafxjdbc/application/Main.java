package org.example.workshopjavafxjdbc.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/workshopjavafxjdbc/MainView.fxml"));
    Parent parent = fxmlLoader.load();
    Scene mainScene = new Scene(parent);

    primaryStage.setScene(mainScene);
    primaryStage.setTitle("Sample JavaFX application");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}