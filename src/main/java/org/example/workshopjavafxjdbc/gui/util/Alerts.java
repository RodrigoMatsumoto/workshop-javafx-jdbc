package org.example.workshopjavafxjdbc.gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {

  public static void showAlert(String title, String header, String content, Alert.AlertType alertType) {
    Alert alert = new Alert(alertType);

    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.show();
  }

  public static Optional<ButtonType> showConfirmation(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);

    return alert.showAndWait();
  }
}