package org.example.workshopjavafxjdbc.model.dao;

import org.example.workshopjavafxjdbc.database.DataBase;
import org.example.workshopjavafxjdbc.model.dao.impl.DepartmentDAOJDBC;
import org.example.workshopjavafxjdbc.model.dao.impl.SellerDAOJDBC;

public class DAOFactory {

  public static SellerDAO createSellerDAO() {
    return new SellerDAOJDBC(DataBase.getConnection());
  }

  public static DepartmentDAO createDepartmentDAO() {
    return new DepartmentDAOJDBC(DataBase.getConnection());
  }
}