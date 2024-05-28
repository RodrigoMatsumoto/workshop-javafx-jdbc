module org.example.workshopjavafxjdbc {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;

  opens org.example.workshopjavafxjdbc.gui to javafx.fxml;
  opens org.example.workshopjavafxjdbc.application to javafx.fxml;
  opens org.example.workshopjavafxjdbc.model.entities to java.base;
  exports org.example.workshopjavafxjdbc.gui;
  exports org.example.workshopjavafxjdbc.application;
  exports org.example.workshopjavafxjdbc.model.entities;
  exports org.example.workshopjavafxjdbc.model.services;
}