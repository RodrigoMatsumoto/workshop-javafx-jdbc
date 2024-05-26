package org.example.workshopjavafxjdbc.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.workshopjavafxjdbc.application.Main;
import org.example.workshopjavafxjdbc.gui.util.Alerts;
import org.example.workshopjavafxjdbc.gui.util.Utils;
import org.example.workshopjavafxjdbc.model.entities.Department;
import org.example.workshopjavafxjdbc.model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

  @FXML
  private TableView<Department> tableViewDepartment;
  @FXML
  private TableColumn<Department, Integer> tableColumnId;
  @FXML
  private TableColumn<Department, String> tableColumnName;
  @FXML
  private Button buttonNew;

  private DepartmentService departmentService;
  private ObservableList<Department> observableDepartmentList;

  @FXML
  public void onButtonNewAction(ActionEvent event) {
    Stage parentStage = Utils.currentStage(event);
    Department department = new Department();

    createDialogForm(department, "/org/example/workshopjavafxjdbc/gui/DepartmentFormView.fxml", parentStage);
  }

  public void setDepartmentService(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }

  public void updateTableView() {
    if (departmentService == null) {
      throw new IllegalStateException("DepartmentService is null");
    }

    List<Department> departmentList = departmentService.findAll();
    observableDepartmentList = FXCollections.observableArrayList(departmentList);
    tableViewDepartment.setItems(observableDepartmentList);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initializeNodes();
  }

  private void initializeNodes() {
    Stage stage = (Stage) Main.getMainScene().getWindow();

    tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
  }

  private void createDialogForm(Department department, String absoluteName, Stage parentStage) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(absoluteName));
      Pane pane = fxmlLoader.load();
      Stage dialogStage = new Stage();
      DepartmentFormController departmentFormController = fxmlLoader.getController();

      departmentFormController.setDepartment(department);
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
}