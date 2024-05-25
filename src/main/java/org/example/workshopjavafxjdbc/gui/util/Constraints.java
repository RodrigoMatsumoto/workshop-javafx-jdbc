package org.example.workshopjavafxjdbc.gui.util;

import javafx.scene.control.TextField;

public class Constraints {

  public static void setTextFieldInteger(TextField textField) {
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null && !newValue.matches("\\d*")) {
        textField.setText(oldValue);
      }
    });
  }

  public static void setTextFieldMaxLength(TextField textField, int maxLength) {
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null && newValue.length() > maxLength) {
        textField.setText(oldValue);
      }
    });
  }

  public static void setTextFieldDouble(TextField textField) {
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?")) {
        textField.setText(oldValue);
      }
    });
  }
}