package org.example.workshopjavafxjdbc.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.example.workshopjavafxjdbc.database.DataBaseException;
import org.example.workshopjavafxjdbc.gui.listeners.DataChangeListener;
import org.example.workshopjavafxjdbc.gui.util.Alerts;
import org.example.workshopjavafxjdbc.gui.util.Constraints;
import org.example.workshopjavafxjdbc.gui.util.Utils;
import org.example.workshopjavafxjdbc.model.entities.Department;
import org.example.workshopjavafxjdbc.model.entities.Seller;
import org.example.workshopjavafxjdbc.model.exception.ValidationException;
import org.example.workshopjavafxjdbc.model.services.DepartmentService;
import org.example.workshopjavafxjdbc.model.services.SellerService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

  @FXML
  private TextField textFieldId;
  @FXML
  private TextField textFieldName;
  @FXML
  private TextField textFieldEmail;
  @FXML
  private DatePicker datePickerBirthDate;
  @FXML
  private TextField textFieldBaseSalary;
  @FXML
  private ComboBox<Department> comboBoxDepartment;
  @FXML
  private Label labelErrorName;
  @FXML
  private Label labelErrorEmail;
  @FXML
  private Label labelErrorBirthDate;
  @FXML
  private Label labelErrorBaseSalary;
  @FXML
  private Button buttonSave;
  @FXML
  private Button buttonCancel;

  private Seller seller;
  private SellerService sellerService;
  private DepartmentService departmentService;
  private final List<DataChangeListener> dataChangeListenerList = new ArrayList<>();
  private ObservableList<Department> departmentObservableList;

  @FXML
  public void onButtonSaveAction(ActionEvent event) {
    if (seller == null) {
      throw new IllegalArgumentException("Seller is null");
    }

    if (sellerService == null) {
      throw new IllegalArgumentException("Seller service is null");
    }

    try {
      seller = getFormData();
      sellerService.saveOrUpdate(seller);

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

  public void setSeller(Seller seller) {
    this.seller = seller;
  }

  public void setServices(SellerService sellerService, DepartmentService departmentService) {
    this.sellerService = sellerService;
    this.departmentService = departmentService;
  }

  public void updateFormData() {
    if (seller == null) {
      throw new IllegalStateException("Seller is null");
    }

    Locale.setDefault(Locale.US);

    textFieldId.setText(String.valueOf(seller.getId()));
    textFieldName.setText(seller.getName());
    textFieldEmail.setText(seller.getEmail());
    textFieldBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));

    if (seller.getBirthDate() != null) {
      datePickerBirthDate.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
    }

    if (seller.getDepartment() == null) {
      comboBoxDepartment.getSelectionModel().selectFirst();
    } else {
      comboBoxDepartment.setValue(seller.getDepartment());
    }
  }

  public void subscribeDataChangeListener(DataChangeListener dataChangeListener) {
    dataChangeListenerList.add(dataChangeListener);
  }

  public void loadAssociatedObjects() {
    if (departmentService == null) {
      throw new IllegalStateException("Department service is null");
    }

    List<Department> departmentList = departmentService.findAll();

    departmentObservableList = FXCollections.observableArrayList(departmentList);
    comboBoxDepartment.setItems(departmentObservableList);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initializeNodes();
  }

  private void initializeNodes() {
    Constraints.setTextFieldInteger(textFieldId);
    Constraints.setTextFieldMaxLength(textFieldName, 70);
    Constraints.setTextFieldDouble(textFieldBaseSalary);
    Constraints.setTextFieldMaxLength(textFieldEmail, 60);
    Utils.formatDatePicker(datePickerBirthDate, "dd/MM/yyyy");

    initializeComboBoxDepartment();
  }

  private Seller getFormData() {
    Seller seller = new Seller();
    ValidationException validationException = new ValidationException("Validation error");

    seller.setId(Utils.tryParseToInt(textFieldId.getText()));

    if (textFieldName.getText() == null || textFieldName.getText().isEmpty()) {
      validationException.addError("name", "Field name is required");
    }

    seller.setName(textFieldName.getText());

    if (textFieldEmail.getText() == null || textFieldEmail.getText().isEmpty()) {
      validationException.addError("email", "Field email is required");
    }

    seller.setEmail(textFieldEmail.getText());

    if (datePickerBirthDate.getValue() == null) {
      validationException.addError("birthDate", "Field birthDate is required");
    } else {
      Instant instant = Instant.from(datePickerBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
      seller.setBirthDate(Date.from(instant));
    }

    if (textFieldBaseSalary.getText() == null || textFieldBaseSalary.getText().isEmpty()) {
      validationException.addError("baseSalary", "Field baseSalary is required");
    }

    seller.setBaseSalary(Utils.tryParseToDouble(textFieldBaseSalary.getText()));

    seller.setDepartment(comboBoxDepartment.getValue());

    if (!validationException.getErrors().isEmpty()) {
      throw validationException;
    }

    return seller;
  }

  private void notifyDataChangeListeners() {
    for (DataChangeListener dataChangeListener : dataChangeListenerList) {
      dataChangeListener.onDataChanged();
    }
  }

  private void setErrorMessages(Map<String, String> errorMessages) {
    Set<String> fields = errorMessages.keySet();

    labelErrorName.setText((fields.contains("name") ? errorMessages.get("name") : ""));
    labelErrorEmail.setText((fields.contains("email") ? errorMessages.get("email") : ""));
    labelErrorBirthDate.setText((fields.contains("birthDate") ? errorMessages.get("birthDate") : ""));
    labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errorMessages.get("baseSalary") : ""));
  }

  private void initializeComboBoxDepartment() {
    Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<>() {
      @Override
      protected void updateItem(Department item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? "" : item.getName());
      }
    };

    comboBoxDepartment.setCellFactory(factory);
    comboBoxDepartment.setButtonCell(factory.call(null));
  }
}