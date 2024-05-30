package org.example.workshopjavafxjdbc.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.workshopjavafxjdbc.database.DataBaseException;
import org.example.workshopjavafxjdbc.gui.listeners.DataChangeListener;
import org.example.workshopjavafxjdbc.gui.util.Alerts;
import org.example.workshopjavafxjdbc.gui.util.Constraints;
import org.example.workshopjavafxjdbc.gui.util.Utils;
import org.example.workshopjavafxjdbc.model.entities.Department;
import org.example.workshopjavafxjdbc.model.exception.ValidationException;
import org.example.workshopjavafxjdbc.model.services.DepartmentService;

import java.net.URL;
import java.util.*;

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
  private DepartmentService departmentService;
  private final List<DataChangeListener> dataChangeListenerList = new ArrayList<>();

  @FXML
  public void onButtonSaveAction(ActionEvent event) {
    if (department == null) {
      throw new IllegalArgumentException("Department is null");
    }

    if (departmentService == null) {
      throw new IllegalArgumentException("Department service is null");
    }

    try {
      department = getFormData();
      departmentService.saveOrUpdate(department);

      notifyDataChangeListeners();

      Utils.currentStage(event).close();
    } catch (ValidationException e) {
      setErrorMessages(e.getErrors());
    } catch (DataBaseException e) {
      Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
    }
  }

  @FXML
  public void onButtonCancelAction(ActionEvent event) {
    Utils.currentStage(event).close();
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public void setDepartmentService(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }

  public void updateFormData() {
    if (department == null) {
      throw new IllegalStateException("Department is null");
    }

    textFieldId.setText(String.valueOf(department.getId()));
    textFieldName.setText(department.getName());
  }

  public void subscribeDataChangeListener(DataChangeListener dataChangeListener) {
    dataChangeListenerList.add(dataChangeListener);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initializeNodes();
  }

  private void initializeNodes() {
    Constraints.setTextFieldInteger(textFieldId);
    Constraints.setTextFieldMaxLength(textFieldName, 30);
  }

  private Department getFormData() {
    Department department = new Department();
    ValidationException validationException = new ValidationException("Validation error");

    department.setId(Utils.tryParseToInt(textFieldId.getText()));

    if (textFieldName.getText() == null || textFieldName.getText().isEmpty()) {
      validationException.addError("name", "Field name is required");
    }

    department.setName(textFieldName.getText());

    if (!validationException.getErrors().isEmpty()) {
      throw validationException;
    }

    return department;
  }

  private void notifyDataChangeListeners() {
    for (DataChangeListener dataChangeListener : dataChangeListenerList) {
      dataChangeListener.onDataChanged();
    }
  }

  private void setErrorMessages(Map<String, String> errorMessages) {
    Set<String> fields = errorMessages.keySet();

    if (fields.contains("name")) {
      labelErrorName.setText(errorMessages.get("name"));
    }
  }
}