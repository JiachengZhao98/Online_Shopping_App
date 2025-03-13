package com.superdupermart.shoppingapp.service.buyer;

import com.superdupermart.shoppingapp.dao.ProductDao;
import com.superdupermart.shoppingapp.dao.UserDao;
import com.superdupermart.shoppingapp.dto.buyer.ProductDto;
import com.superdupermart.shoppingapp.dto.buyer.WatchlistRequestDto;
import com.superdupermart.shoppingapp.entity.Product;
import com.superdupermart.shoppingapp.entity.User;
import com.superdupermart.shoppingapp.entity.Watchlist;
import com.superdupermart.shoppingapp.exception.ResourceNotFoundException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class WatchlistServiceImpl implements WatchlistService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    private User getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    @Transactional
    public void addProductToWatchlist(WatchlistRequestDto request) {
        User user = getLoggedInUser();
        Session session = entityManager.unwrap(Session.class);
        user = session.get(User.class, user.getId());
        Watchlist existing = (Watchlist) session
                .createQuery("FROM Watchlist w WHERE w.user.id = :user_id AND w.product.id = :product_id")
                .setParameter("user_id", user.getId())
                .setParameter("product_id", request.getProductId())
                .uniqueResult();
        if(existing != null) {
            throw new RuntimeException("Product already in watchlist");
        }
        Product product = productDao.getById(request.getProductId());
        if(product == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setProduct(product);
        session.save(watchlist);
    }


    @Override
    @Transactional
    public void removeProductFromWatchlist(Long productId) {
        User user = getLoggedInUser();
        Session session = entityManager.unwrap(Session.class);
        Watchlist existing = (Watchlist) session
                .createQuery("FROM Watchlist w WHERE w.user.username = :username AND w.product.id = :productId")
                .setParameter("username", user.getUsername())
                .setParameter("productId", productId)
                .uniqueResult();
        if(existing == null) {
            throw new RuntimeException("Product not in watchlist");
        }
        session.delete(existing);
    }

    @Override
    @Transactional
    public List<ProductDto> getWatchlistProducts() {
        User user = getLoggedInUser();
        Session session = entityManager.unwrap(Session.class);
        List<Watchlist> watchlistItems = session
                .createQuery("FROM Watchlist w WHERE w.user.username = :username", Watchlist.class)
                .setParameter("username", user.getUsername())
                .list();
        if (watchlistItems.isEmpty()) {
            return null;
        }
        return watchlistItems.stream().map(w -> {
            Product product = w.getProduct();
            if(product.getStock() > 0) {
                ProductDto dto = new ProductDto();
                dto.setName(product.getProductName());
                dto.setDescription(product.getDescription());
                dto.setRetailPrice(product.getRetailPrice());
                return dto;
            }
            return null;
        }).filter(dto -> dto != null).collect(Collectors.toList());
    }
}
