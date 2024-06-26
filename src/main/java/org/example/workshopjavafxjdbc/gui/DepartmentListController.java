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
import org.example.workshopjavafxjdbc.model.entities.Department;
import org.example.workshopjavafxjdbc.model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable, DataChangeListener {

  @FXML
  private TableView<Department> tableViewDepartment;
  @FXML
  private TableColumn<Department, Integer> tableColumnId;
  @FXML
  private TableColumn<Department, String> tableColumnName;
  @FXML
  private TableColumn<Department, Department> tableColumnEdit;
  @FXML
  private TableColumn<Department, Department> tableColumnRemove;
  @FXML
  private Button buttonNew;

  private DepartmentService departmentService;

  @FXML
  public void onButtonNewAction(ActionEvent event) {
    Stage parentStage = Utils.currentStage(event);
    Department department = new Department();

    createDialogForm(department, parentStage);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initializeNodes();
  }

  @Override
  public void onDataChanged() {
    updateTableView();
  }

  public void setDepartmentService(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }

  public void updateTableView() {
    if (departmentService == null) {
      throw new IllegalStateException("DepartmentService is null");
    }

    List<Department> departmentList = departmentService.findAll();

    ObservableList<Department> observableDepartmentList = FXCollections.observableArrayList(departmentList);
    tableViewDepartment.setItems(observableDepartmentList);
    initializeEditButtons();
    initializeRemoveButtons();
  }

  private void createDialogForm(Department department, Stage parentStage) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
          "/org/example/workshopjavafxjdbc/gui/DepartmentFormView.fxml"));
      Pane pane = fxmlLoader.load();
      Stage dialogStage = new Stage();
      DepartmentFormController departmentFormController = fxmlLoader.getController();

      departmentFormController.setDepartment(department);
      departmentFormController.setDepartmentService(new DepartmentService());
      departmentFormController.subscribeDataChangeListener(this);
      departmentFormController.updateFormData();

      dialogStage.setTitle("Enter department data");
      dialogStage.setScene(new Scene(pane));
      dialogStage.setResizable(false);
      dialogStage.initOwner(parentStage);
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.showAndWait();
    } catch (IOException e) {
      Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
    }
  }

  private void initializeEditButtons() {
    tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    tableColumnEdit.setCellFactory(param -> new TableCell<>() {
      private final Button buttonEdit = new Button("edit");

      @Override
      protected void updateItem(Department department, boolean empty) {
        super.updateItem(department, empty);

        if (department == null) {
          setGraphic(null);
          return;
        }

        setGraphic(buttonEdit);

        buttonEdit.setOnAction(
            event -> createDialogForm(
                department,
                Utils.currentStage(event)
            )
        );
      }
    });
  }

  private void initializeNodes() {
    Stage stage = (Stage) Main.getMainScene().getWindow();

    tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
  }

  private void initializeRemoveButtons() {
    tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    tableColumnRemove.setCellFactory(param -> new TableCell<>() {

      private final Button buttonRemove = new Button("remove");

      @Override
      protected void updateItem(Department department, boolean empty) {
        super.updateItem(department, empty);

        if (department == null) {
          setGraphic(null);
          return;
        }
        setGraphic(buttonRemove);
        buttonRemove.setOnAction(event -> removeEntity(department));
      }
    });
  }

  private void removeEntity(Department department) {
    Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

    if (result.isPresent() && result.get() == ButtonType.OK) {
      if (departmentService == null) {
        throw new IllegalStateException("DepartmentService is null");
      }

      try {
        departmentService.remove(department);
        updateTableView();
      } catch (DataBaseException e) {
        Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
      }
    }
  }
}