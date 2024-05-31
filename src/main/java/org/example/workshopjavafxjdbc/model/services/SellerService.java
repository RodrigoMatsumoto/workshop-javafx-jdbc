package org.example.workshopjavafxjdbc.model.services;

import org.example.workshopjavafxjdbc.model.dao.DAOFactory;
import org.example.workshopjavafxjdbc.model.dao.SellerDAO;
import org.example.workshopjavafxjdbc.model.entities.Seller;

import java.util.List;

public class SellerService {

  private final SellerDAO sellerDAO = DAOFactory.createSellerDAO();

  public List<Seller> findAll() {
    return sellerDAO.findAll();
  }

  public void remove(Seller seller) {
    sellerDAO.deleteById(seller.getId());
  }

  public void saveOrUpdate(Seller seller) {
    if (seller.getId() == null) {
      sellerDAO.insert(seller);
    } else {
      sellerDAO.update(seller);
    }
  }
}