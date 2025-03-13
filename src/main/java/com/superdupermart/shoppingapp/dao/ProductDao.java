package com.superdupermart.shoppingapp.dao;

import com.superdupermart.shoppingapp.entity.Product;
import java.util.List;

public interface ProductDao {
    Product getByName(String name);
    Product getById(Long id);
    List<Product> getAllInStock();
    List<Product> getAll();
    void save(Product product);
    void update(Product product);
}
