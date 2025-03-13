package com.superdupermart.shoppingapp.dao;

import com.superdupermart.shoppingapp.entity.Order;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Order order) {
        Session session = entityManager.unwrap(Session.class);
        session.save(order);
    }

    @Override
    public Order getById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Order.class, id);
    }

    @Override
    public List<Order> getOrdersByUser(String username) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM Order o WHERE o.user.username = :username", Order.class)
                .setParameter("username", username)
                .list();
    }

    @Override
    public List<Order> getOrdersForSeller(int page, int size) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM Order", Order.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .list();
    }

    @Override
    public void update(Order order) {
        Session session = entityManager.unwrap(Session.class);
        session.update(order);
    }
}
