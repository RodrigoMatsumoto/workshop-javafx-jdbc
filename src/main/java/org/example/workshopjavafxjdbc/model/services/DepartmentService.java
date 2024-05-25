package org.example.workshopjavafxjdbc.model.services;

import org.example.workshopjavafxjdbc.model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

  public List<Department> findAll() {
    List<Department> departmentList = new ArrayList<>();

    departmentList.add(new Department(1, "Books"));
    departmentList.add(new Department(2, "Computers"));
    departmentList.add(new Department(3, "Electronics"));

    return departmentList;
  }
}