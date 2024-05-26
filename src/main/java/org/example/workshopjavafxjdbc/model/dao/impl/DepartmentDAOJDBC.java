package org.example.workshopjavafxjdbc.model.dao.impl;

import org.example.workshopjavafxjdbc.database.DataBaseException;
import org.example.workshopjavafxjdbc.model.dao.DepartmentDAO;
import org.example.workshopjavafxjdbc.model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOJDBC implements DepartmentDAO {

  private final Connection connection;

  public DepartmentDAOJDBC(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void insert(Department department) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
          "INSERT INTO department " +
              "(Name) " +
              "VALUES " +
              "(?)",
          Statement.RETURN_GENERATED_KEYS);
    ) {
      preparedStatement.setString(1, department.getName());

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected > 0) {
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
          int id = resultSet.getInt(1);
          department.setId(id);
        }
      } else {
        throw new DataBaseException("Unexpected error! No rows affected!");
      }
    } catch (SQLException e) {
      throw new DataBaseException(e.getMessage());
    }
  }

  @Override
  public void update(Department department) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
          "UPDATE department " +
              "SET Name = ? " +
              "WHERE Id = ?");
    ) {
      preparedStatement.setString(1, department.getName());
      preparedStatement.setInt(2, department.getId());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new DataBaseException(e.getMessage());
    }
  }

  @Override
  public void deleteById(Integer id) {
    try(PreparedStatement preparedStatement = connection.prepareStatement(
        "DELETE FROM department WHERE Id = ?");
    ) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new DataBaseException(e.getMessage());
    }
  }

  @Override
  public Department findById(Integer id) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT * FROM department WHERE Id = ?");
    ) {
      preparedStatement.setInt(1, id);

      try (ResultSet resultSet = preparedStatement.executeQuery();) {
        if (resultSet.next()) {
          return extractDepartmentFromResultSet(resultSet);
        }
        return null;
      }
    } catch (SQLException e) {
      throw new DataBaseException(e.getMessage());
    }
  }

  @Override
  public List<Department> findAll() {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
      "SELECT * FROM department ORDER BY Name");
         ResultSet resultSet = preparedStatement.executeQuery();
    ) {
      List<Department> departmentList = new ArrayList<>();

      while (resultSet.next()) {
        departmentList.add(extractDepartmentFromResultSet(resultSet));
      }
      return departmentList;
    } catch (SQLException e) {
      throw new DataBaseException(e.getMessage());
    }
  }

  private static Department extractDepartmentFromResultSet(ResultSet resultSet) throws SQLException {
    Department department = new Department();

    department.setId(resultSet.getInt("Id"));
    department.setName(resultSet.getString("Name"));

    return department;
  }
}