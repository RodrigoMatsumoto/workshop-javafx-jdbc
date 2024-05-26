package org.example.workshopjavafxjdbc.model.dao.impl;

import org.example.workshopjavafxjdbc.database.DataBaseException;
import org.example.workshopjavafxjdbc.model.dao.SellerDAO;
import org.example.workshopjavafxjdbc.model.entities.Department;
import org.example.workshopjavafxjdbc.model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDAOJDBC implements SellerDAO {

  private final Connection connection;

  public SellerDAOJDBC(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void insert(Seller seller) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
      "INSERT INTO seller " +
          "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
          "VALUES " +
          "(?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS);
    ) {
      setSellersParameters(seller, preparedStatement);

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected > 0) {
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
          if (resultSet.next()) {
            int id = resultSet.getInt(1);
            seller.setId(id);
          }
        }
      } else {
        throw new DataBaseException("Unexpected error! No rows affected!");
      }
    } catch (SQLException e) {
      throw new DataBaseException(e.getMessage());
    }
  }

  @Override
  public void update(Seller seller) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "UPDATE seller "+
            "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
            "WHERE Id = ?");
    ) {
      setSellersParameters(seller, preparedStatement);
      preparedStatement.setInt(6, seller.getId());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new DataBaseException(e.getMessage());
    }
  }

  @Override
  public void deleteById(Integer id) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "DELETE FROM seller WHERE Id = ?");
    ) {
      preparedStatement.setInt(1, id);
    } catch (SQLException e) {
      throw new DataBaseException(e.getMessage());
    }
  }

  @Override
  public Seller findById(Integer id) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT seller.*, department.Name as DepName " +
            "FROM seller INNER JOIN department " +
            "ON seller.DepartmentId = department.Id " +
            "WHERE seller.Id = ?"
    );
    ) {
      preparedStatement.setInt(1, id);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          Department department = instantiateDepartment(resultSet);
          Seller seller = instantiateSeller(resultSet, department);

          return seller;
        }
        return null;
    }
  } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Seller> findAll() {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT seller.*, department.Name as DepName " +
            "FROM seller INNER JOIN department " +
            "ON seller.DepartmentId = department.Id " +
            "ORDER BY Name");
        ResultSet resultSet = preparedStatement.executeQuery();
    ) {
      return extractSellersFromResultSet(resultSet);
    } catch (SQLException e) {
      throw new DataBaseException(e.getMessage());
    }
  }

  @Override
  public List<Seller> findByDepartment(Department department) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT seller.*, department.Name as DepName " +
            "FROM seller INNER JOIN department " +
            "ON seller.DepartmentId = department.Id " +
            "WHERE DepartmentId = ?" +
            "ORDER BY Name");
         ResultSet resultSet = preparedStatement.executeQuery();
    ) {
      preparedStatement.setInt(1, department.getId());

      return extractSellersFromResultSet(resultSet);
    } catch (SQLException e) {
      throw new DataBaseException(e.getMessage());
    }
  }

  private void setSellersParameters(Seller seller, PreparedStatement preparedStatement) throws SQLException {
    preparedStatement.setString(1, seller.getName());
    preparedStatement.setString(2, seller.getEmail());
    preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
    preparedStatement.setDouble(4, seller.getBaseSalary());
    preparedStatement.setInt(5, seller.getDepartment().getId());
  }

  private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
    Seller seller = new Seller();

    seller.setId(resultSet.getInt("Id"));
    seller.setName(resultSet.getString("Name"));
    seller.setEmail(resultSet.getString("Email"));
    seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
    seller.setBirthDate(resultSet.getDate("BirthDate"));
    seller.setDepartment(department);

    return seller;
  }

  private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
    Department department = new Department();

    department.setId(resultSet.getInt("DepartmentId"));
    department.setName(resultSet.getString("DepName"));

    return department;
  }

  private List<Seller> extractSellersFromResultSet(ResultSet resultSet) throws SQLException {
    List<Seller> sellerList = new ArrayList<>();
    Map<Integer, Department> departmentMap = new HashMap<>();

    while (resultSet.next()) {
      Department dep = departmentMap.get(resultSet.getInt("DepartmentId"));

      if(dep == null) {
        departmentMap.put(resultSet.getInt("DepartmentId"), instantiateDepartment(resultSet));
      }
      sellerList.add(instantiateSeller(resultSet, dep));
    }
    return sellerList;
  }
}