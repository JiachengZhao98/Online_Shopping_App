package com.superdupermart.shoppingapp.dao;

import com.superdupermart.shoppingapp.entity.Order;
import java.util.List;

public interface OrderDao {
    void save(Order order);
    Order getById(Long id);
    List<Order> getOrdersByUser(String username);
    List<Order> getOrdersForSeller(int page, int size);
    void update(Order order);
}
