package org.example.workshopjavafxjdbc.model.dao;

import org.example.workshopjavafxjdbc.model.entities.Seller;

import java.util.List;

public interface SellerDAO {

  void deleteById(Integer id);

  List<Seller> findAll();

  void insert(Seller seller);

  void update(Seller seller);

}