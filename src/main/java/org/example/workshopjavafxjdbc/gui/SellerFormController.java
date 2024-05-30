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
import org.example.workshopjavafxjdbc.model.entities.Seller;
import org.example.workshopjavafxjdbc.model.exception.ValidationException;
import org.example.workshopjavafxjdbc.model.services.SellerService;

import java.net.URL;
import java.util.*;

public class SellerFormController implements Initializable {

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

  private Seller seller;
  private SellerService sellerService;
  private final List<DataChangeListener> dataChangeListenerList = new ArrayList<>();

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

  public void setSellerService(SellerService sellerService) {
    this.sellerService = sellerService;
  }

  public void updateFormData() {
    if (seller == null) {
      throw new IllegalStateException("Seller is null");
    }

    textFieldId.setText(String.valueOf(seller.getId()));
    textFieldName.setText(seller.getName());
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

  private Seller getFormData() {
    Seller seller = new Seller();
    ValidationException validationException = new ValidationException("Validation error");

    seller.setId(Utils.tryParseToInt(textFieldId.getText()));

    if (textFieldName.getText() == null || textFieldName.getText().isEmpty()) {
      validationException.addError("name", "Field name is required");
    }

    seller.setName(textFieldName.getText());

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

    if (fields.contains("name")) {
      labelErrorName.setText(errorMessages.get("name"));
    }
  }
}