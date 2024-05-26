package org.example.workshopjavafxjdbc.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBase {

  private static Connection connection = null;

  public static Connection getConnection() {
    if (connection == null) {
      try {
        Properties properties = loadProperties();
        String url = properties.getProperty("databaseurl");

        connection = DriverManager.getConnection(url, properties);
      } catch (SQLException e) {
        throw new DataBaseException(e.getMessage());
      }
    }

    return connection;
  }

  private static Properties loadProperties() {
    try (InputStream inputStream = DataBase.class.getClassLoader().getResourceAsStream("database.properties")) {
      Properties properties = new Properties();

      properties.load(inputStream);

      return properties;
    } catch (IOException e) {
      throw new DataBaseException(e.getMessage());
    }
  }
}