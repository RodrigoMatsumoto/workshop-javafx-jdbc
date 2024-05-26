package org.example.workshopjavafxjdbc.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.workshopjavafxjdbc.gui.util.Constraints;
import org.example.workshopjavafxjdbc.model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

  @FXML
  private TextField textFieldId;
  @FXML
  private TextField textFieldName;
  @FXML
  private Label labelErrorName;
  @FXML
  private Button buttonSave;
  @FXML
  private Button buttonCancel;

  private Department department;

  @FXML
  public void onButtonSaveAction() {
    System.out.println("onButtonSave");
  }

  @FXML
  public void onButtonCancelAction() {
    System.out.println("onButtonCancel");
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public void updateFormData() {
    if (department == null) {
      throw new IllegalStateException("Department is null");
    }

    textFieldId.setText(String.valueOf(department.getId()));
    textFieldName.setText(department.getName());
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initializeNodes();
  }

  private void initializeNodes() {
    Constraints.setTextFieldInteger(textFieldId);
    Constraints.setTextFieldMaxLength(textFieldName, 30);
  }
}