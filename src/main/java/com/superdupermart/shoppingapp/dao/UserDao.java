package com.superdupermart.shoppingapp.dao;

import com.superdupermart.shoppingapp.entity.User;

public interface UserDao {
    void save(User user);
    User findByUsername(String username);
    User findByEmail(String email);
}
