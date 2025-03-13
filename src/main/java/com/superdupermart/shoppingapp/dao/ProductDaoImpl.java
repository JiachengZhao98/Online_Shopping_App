package com.superdupermart.shoppingapp.dao;

import com.superdupermart.shoppingapp.entity.Product;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product getByName(String productName) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM Product p WHERE p.productName = :productName", Product.class)
                  .setParameter("productName", productName)
                  .uniqueResult();
    }

    @Override
    public Product getById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Product.class, id);
    }

    @Override
    public List<Product> getAllInStock() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> productRoot = cq.from(Product.class);
        cq.select(productRoot)
                .where(cb.greaterThan(productRoot.get("stock"), 0));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Product> getAll() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM Product", Product.class).list();
    }

    @Override
    public void save(Product product) {
        Session session = entityManager.unwrap(Session.class);
        session.save(product);
    }

    @Override
    public void update(Product product) {
        Session session = entityManager.unwrap(Session.class);
        session.update(product);
    }
}
