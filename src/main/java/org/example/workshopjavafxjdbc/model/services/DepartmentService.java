package org.example.workshopjavafxjdbc.model.services;

import org.example.workshopjavafxjdbc.model.dao.DAOFactory;
import org.example.workshopjavafxjdbc.model.dao.DepartmentDAO;
import org.example.workshopjavafxjdbc.model.entities.Department;

import java.util.List;

public class DepartmentService {

  private final DepartmentDAO departmentDAO = DAOFactory.createDepartmentDAO();

  public List<Department> findAll() {
    return departmentDAO.findAll();
  }

  public void saveOrUpdate(Department department) {
    if (department.getId() == null) {
      departmentDAO.insert(department);
    } else {
      departmentDAO.update(department);
    }
  }

  public void remove(Department department) {
    departmentDAO.deleteById(department.getId());
  }
}