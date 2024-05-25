module org.example.workshopjavafxjdbc {
  requires javafx.controls;
  requires javafx.fxml;


  opens org.example.workshopjavafxjdbc to javafx.fxml;
  opens org.example.workshopjavafxjdbc.application to javafx.fxml;
  opens org.example.workshopjavafxjdbc.gui to javafx.fxml;
  exports org.example.workshopjavafxjdbc.application;
  exports org.example.workshopjavafxjdbc.gui;
}