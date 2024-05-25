package org.example.workshopjavafxjdbc.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

  private static Scene mainScene;

  @Override
  public void start(Stage primaryStage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/workshopjavafxjdbc/gui/MainView.fxml"));
    ScrollPane scrollPane = fxmlLoader.load();

    mainScene = new Scene(scrollPane);

    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);

    primaryStage.setScene(mainScene);
    primaryStage.setTitle("Sample JavaFX application");
    primaryStage.show();
  }

  public static Scene getMainScene() {
    return mainScene;
  }

  public static void main(String[] args) {
    launch();
  }
}