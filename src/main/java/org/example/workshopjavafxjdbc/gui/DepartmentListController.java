package org.example.workshopjavafxjdbc.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.workshopjavafxjdbc.application.Main;
import org.example.workshopjavafxjdbc.model.entities.Department;
import org.example.workshopjavafxjdbc.model.services.DepartmentService;

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
  public void onButtonNewAction() {
    System.out.println("onButtonNewAction");
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
}