package com.superdupermart.shoppingapp.service.buyer;

import com.superdupermart.shoppingapp.dao.ProductDao;
import com.superdupermart.shoppingapp.dto.buyer.ProductDto;
import com.superdupermart.shoppingapp.dao.OrderDao;
import com.superdupermart.shoppingapp.dao.UserDao;
import com.superdupermart.shoppingapp.entity.Order;
import com.superdupermart.shoppingapp.entity.OrderItem;
import com.superdupermart.shoppingapp.entity.Product;
import com.superdupermart.shoppingapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderSummaryServiceImpl implements OrderSummaryService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;


    private User getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getTopFrequentProducts() {
        // Implement logic to return top 3 frequent products (including canceled orders)
        User user = getLoggedInUser();
        List<Order> orders = orderDao.getOrdersByUser(user.getUsername());
        Map<Long, Long> productFrequencyMap = new HashMap<>();
        for (Order order : orders) {
            for (var item : order.getItems()) {
                Long productId = item.getProduct().getId();
                long frequency = item.getQuantity();
                productFrequencyMap.put(productId,
                            productFrequencyMap.getOrDefault(productId, 0L) + frequency);
            }
        }
        // Sort product ids by total quantity (frequency) descending,
        // then by product id ascending as a tie-breaker; pick the top 3.
        List<Long> topProductIds = productFrequencyMap.entrySet().stream()
                .sorted((e1, e2) -> {
                    int cmp = e2.getValue().compareTo(e1.getValue());
                    return (cmp != 0) ? cmp : e1.getKey().compareTo(e2.getKey());
                })
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<ProductDto> productDtos = new ArrayList<>();
        for (Long productId : topProductIds) {
            ProductDto dto = new ProductDto();
            Product product = productDao.getById(productId);
            dto.setName(product.getProductName());
            dto.setDescription(product.getDescription());
            dto.setRetailPrice(product.getRetailPrice());
            productDtos.add(dto);
        }
        return productDtos;

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getTopRecentProducts() {

        User user = getLoggedInUser();
        // Retrieve all orders (including canceled orders)
        List<Order> allOrders = orderDao.getOrdersByUser(user.getUsername());

        // For each product, track its most recent order date
        Map<Long, Date> productRecentDate = new HashMap<>();
        for (Order order : allOrders) {
            for (OrderItem item : order.getItems()) {
                Long productId = item.getProduct().getId();
                Date orderDate = order.getOrderDate();
                if (!productRecentDate.containsKey(productId)) {
                    productRecentDate.put(productId, orderDate);
                } else {
                    Date recentDate = productRecentDate.get(productId);
                    if (orderDate.after(recentDate)) {
                        productRecentDate.put(productId, orderDate);
                    }
                }
            }
        }

        // Sort product ids by the most recent purchase date descending
        List<Long> sortedProductIds = productRecentDate.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .map(Map.Entry::getKey)
            .limit(3)
            .collect(Collectors.toList());

        // Convert product ids to ProductDto objects
        List<ProductDto> result = new ArrayList<>();
        for (Long productId : sortedProductIds) {
            Product product = productDao.getById(productId);
            if (product != null) {
                ProductDto dto = new ProductDto();
                dto.setName(product.getProductName());
                dto.setRetailPrice(product.getRetailPrice());
                dto.setDescription(product.getDescription());
                result.add(dto);
            }
        }
        return result;
    }
}
