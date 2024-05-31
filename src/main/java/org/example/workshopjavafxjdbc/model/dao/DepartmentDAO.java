package org.example.workshopjavafxjdbc.model.dao;

import org.example.workshopjavafxjdbc.model.entities.Department;

import java.util.List;

public interface DepartmentDAO {

  void deleteById(Integer id);

  List<Department> findAll();

  void insert(Department department);

  void update(Department department);
}