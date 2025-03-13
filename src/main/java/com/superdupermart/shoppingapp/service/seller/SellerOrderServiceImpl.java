package com.superdupermart.shoppingapp.service.seller;

import com.superdupermart.shoppingapp.dao.OrderDao;
import com.superdupermart.shoppingapp.dao.ProductDao;
import com.superdupermart.shoppingapp.dao.UserDao;
import com.superdupermart.shoppingapp.dto.seller.SellerOrderDto;
import com.superdupermart.shoppingapp.entity.Order;
import com.superdupermart.shoppingapp.entity.OrderItem;
import com.superdupermart.shoppingapp.entity.Product;
import com.superdupermart.shoppingapp.entity.User;
import com.superdupermart.shoppingapp.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SellerOrderServiceImpl implements SellerOrderService {

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
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    public void completeOrder(Long orderId) {
        User user = getLoggedInUser();
        if (!user.getRole().equalsIgnoreCase("ROLE_SELLER")) {
            throw new ResourceNotFoundException("Not A Seller, Access Denied");
        }
        Order order = orderDao.getById(orderId);
        if ("Canceled".equalsIgnoreCase(order.getStatus())) {
            throw new ResourceNotFoundException("Order has already been canceled");
        }
        if ("Completed".equalsIgnoreCase(order.getStatus())) {
            throw new ResourceNotFoundException("Order has already been completed");
        }
        order.setStatus("Completed");
        orderDao.update(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        User user = getLoggedInUser();
        if (!user.getRole().equalsIgnoreCase("ROLE_SELLER")) {
            throw new ResourceNotFoundException("Not A Seller, Access Denied");
        }
        Order order = orderDao.getById(orderId);
        if (order == null || !"Processing".equalsIgnoreCase(order.getStatus())) {
            throw new ResourceNotFoundException("Order cannot be canceled");
        }
        // Restore stock for each item in the order
        for (OrderItem item : order.getItems()) {
            Product product = productDao.getById(item.getProduct().getId());
            if (product != null) {
                product.setStock(product.getStock() + item.getQuantity());
                productDao.update(product);
            }
        }
        order.setStatus("Canceled");
        orderDao.update(order);
    }

    @Override
    public List<SellerOrderDto> getDashboardOrders(int page, int size) {
        User user = getLoggedInUser();
        if (!user.getRole().equalsIgnoreCase("ROLE_SELLER")) {
            throw new ResourceNotFoundException("Not A Seller, Access Denied");
        }
        List<Order> orders = orderDao.getOrdersForSeller(page, size);
        return orders.stream()
                .map(order -> {
                    SellerOrderDto dto = new SellerOrderDto();
                    dto.setOrderId(order.getId());
                    dto.setOrderTime(order.getOrderDate());
                    dto.setStatus(order.getStatus());
                    if (order.getTotalAmount() != null) dto.setTotalAmount(order.getTotalAmount());
                    else dto.setTotalAmount(0.0);
                    if (order.getUser() != null) {
                        dto.setBuyerUsername(order.getUser().getUsername());
                    }
                    // Use the snapshot product name stored in OrderItem
                    List<String> itemNames = order.getItems().stream()
                            .map(OrderItem::getProductName)
                            .collect(Collectors.toList());
                    dto.setItems(itemNames);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public SellerOrderDto getSellerOrderDetail(Long orderId) {
        User user = getLoggedInUser();
        if (!user.getRole().equalsIgnoreCase("ROLE_SELLER")) {
            throw new ResourceNotFoundException("Not A Seller, Access Denied");
        }
        Order order = orderDao.getById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found");
        }
        SellerOrderDto dto = new SellerOrderDto();
        dto.setOrderId(order.getId());
        dto.setOrderTime(order.getOrderDate());
        dto.setStatus(order.getStatus());
        if (order.getUser() != null) {
            dto.setBuyerUsername(order.getUser().getUsername());
        }
        List<String> itemNames = order.getItems().stream()
                .map(OrderItem::getProductName)
                .collect(Collectors.toList());
        dto.setItems(itemNames);
        return dto;
    }
}
