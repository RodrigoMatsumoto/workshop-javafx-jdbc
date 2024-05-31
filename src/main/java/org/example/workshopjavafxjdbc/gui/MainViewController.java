package org.example.workshopjavafxjdbc.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.example.workshopjavafxjdbc.application.Main;
import org.example.workshopjavafxjdbc.gui.util.Alerts;
import org.example.workshopjavafxjdbc.model.services.DepartmentService;
import org.example.workshopjavafxjdbc.model.services.SellerService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController implements Initializable {

  @FXML
  private MenuItem menuItemSeller;
  @FXML
  private MenuItem menuItemDepartment;
  @FXML
  private MenuItem menuItemAbout;

  @FXML
  public void onMenuItemAboutAction() {
    loadView("/org/example/workshopjavafxjdbc/gui/AboutView.fxml", x -> {});
  }

  @FXML
  public void onMenuItemDepartmentAction() {
    loadView("/org/example/workshopjavafxjdbc/gui/DepartmentListView.fxml",
        (DepartmentListController departmentListController) -> {
          departmentListController.setDepartmentService(new DepartmentService());
          departmentListController.updateTableView();
        });
  }

  @FXML
  public void onMenuItemSellerAction() {
    loadView("/org/example/workshopjavafxjdbc/gui/SellerListView.fxml",
        (SellerListController sellerListController) -> {
          sellerListController.setSellerService(new SellerService());
          sellerListController.updateTableView();
        });
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
  }

  private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(absoluteName));
      VBox newVBox = fxmlLoader.load();
      Scene mainScene = Main.getMainScene();
      VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
      Node mainMenu = mainVBox.getChildren().getFirst();

      mainVBox.getChildren().clear();
      mainVBox.getChildren().add(mainMenu);
      mainVBox.getChildren().addAll(newVBox.getChildren());
      T controller = fxmlLoader.getController();
      initializingAction.accept(controller);
    } catch (IOException e) {
      Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
    }
  }
}