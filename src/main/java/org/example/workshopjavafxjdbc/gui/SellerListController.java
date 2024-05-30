package org.example.workshopjavafxjdbc.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.workshopjavafxjdbc.application.Main;
import org.example.workshopjavafxjdbc.database.DataBaseException;
import org.example.workshopjavafxjdbc.gui.listeners.DataChangeListener;
import org.example.workshopjavafxjdbc.gui.util.Alerts;
import org.example.workshopjavafxjdbc.gui.util.Utils;
import org.example.workshopjavafxjdbc.model.entities.Seller;
import org.example.workshopjavafxjdbc.model.services.SellerService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerListController implements Initializable, DataChangeListener {

  @FXML
  private TableView<Seller> tableViewSeller;
  @FXML
  private TableColumn<Seller, Integer> tableColumnId;
  @FXML
  private TableColumn<Seller, String> tableColumnName;
  @FXML
  private TableColumn<Seller, Seller> tableColumnEdit;
  @FXML
  private TableColumn<Seller, Seller> tableColumnRemove;
  @FXML
  private TableColumn<Seller, String> tableColumnEmail;
  @FXML
  private TableColumn<Seller, Date> tableColumnBirthDate;
  @FXML
  private TableColumn<Seller, Double> tableColumnBaseSalary;
  @FXML
  private Button buttonNew;

  private SellerService sellerService;
  private ObservableList<Seller> observableSellerList;

  @FXML
  public void onButtonNewAction(ActionEvent event) {
    Stage parentStage = Utils.currentStage(event);
    Seller seller = new Seller();

    createDialogForm(seller, "/org/example/workshopjavafxjdbc/gui/SellerFormView.fxml", parentStage);
  }

  public void setSellerService(SellerService sellerService) {
    this.sellerService = sellerService;
  }

  public void updateTableView() {
    if (sellerService == null) {
      throw new IllegalStateException("SellerService is null");
    }

    List<Seller> sellerList = sellerService.findAll();

    observableSellerList = FXCollections.observableArrayList(sellerList);
    tableViewSeller.setItems(observableSellerList);
    initEditButtons();
    initRemoveButtons();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initializeNodes();
  }

  @Override
  public void onDataChanged() {
    updateTableView();
  }

  private void initializeNodes() {
    Stage stage = (Stage) Main.getMainScene().getWindow();
    Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
    Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);

    tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
    tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
    tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
  }

  private void createDialogForm(Seller seller, String absoluteName, Stage parentStage) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(absoluteName));
      Pane pane = fxmlLoader.load();
      Stage dialogStage = new Stage();
      SellerFormController sellerFormController = fxmlLoader.getController();

      sellerFormController.setSeller(seller);
      sellerFormController.setSellerService(new SellerService());
      sellerFormController.subscribeDataChangeListener(this);
      sellerFormController.updateFormData();

      dialogStage.setTitle("Enter seller data");
      dialogStage.setScene(new Scene(pane));
      dialogStage.setResizable(false);
      dialogStage.initOwner(parentStage);
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.showAndWait();
    } catch (IOException e) {
      Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
    }
  }

  private void initEditButtons() {
    tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    tableColumnEdit.setCellFactory(param -> new TableCell<>() {
      private final Button buttonEdit = new Button("edit");

      @Override
      protected void updateItem(Seller seller, boolean empty) {
        super.updateItem(seller, empty);

        if (seller == null) {
          setGraphic(null);
          return;
        }

        setGraphic(buttonEdit);

        buttonEdit.setOnAction(
          event -> createDialogForm(
            seller, "/org/example/workshopjavafxjdbc/gui/SellerFormView.fxml",
              Utils.currentStage(event)
          )
        );
      }
    });
  }

  private void initRemoveButtons() {
    tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    tableColumnRemove.setCellFactory(param -> new TableCell<>() {

      private final Button buttonRemove = new Button("remove");

      @Override
      protected void updateItem(Seller seller, boolean empty) {
        super.updateItem(seller, empty);

        if (seller == null) {
          setGraphic(null);
          return;
        }
        setGraphic(buttonRemove);
        buttonRemove.setOnAction(event -> removeEntity(seller));
      }
    });
  }

  private void removeEntity(Seller seller) {
    Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

    if (result.get() == ButtonType.OK) {
      if (sellerService == null) {
        throw new IllegalStateException("SellerService is null");
      }

      try {
        sellerService.remove(seller);
        updateTableView();
      } catch (DataBaseException e) {
        Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
      }
    }
  }
}