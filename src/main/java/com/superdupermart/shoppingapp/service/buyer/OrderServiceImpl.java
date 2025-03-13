package com.superdupermart.shoppingapp.service.buyer;

import com.superdupermart.shoppingapp.dao.OrderDao;
import com.superdupermart.shoppingapp.dao.ProductDao;
import com.superdupermart.shoppingapp.dao.UserDao;
import com.superdupermart.shoppingapp.dto.buyer.OrderRequestDto;
import com.superdupermart.shoppingapp.dto.buyer.OrderDto;
import com.superdupermart.shoppingapp.dto.buyer.OrderItemDto;
import com.superdupermart.shoppingapp.entity.Order;
import com.superdupermart.shoppingapp.entity.OrderItem;
import com.superdupermart.shoppingapp.entity.Product;
import com.superdupermart.shoppingapp.entity.User;
import com.superdupermart.shoppingapp.exception.NotEnoughInventoryException;
import com.superdupermart.shoppingapp.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    // Retrieve the currently logged-in user from the database.
    private User getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    @Transactional
    public void placeOrder(OrderRequestDto orderRequest) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setStatus("Processing");
        order.setUser(getLoggedInUser());
        double amount = 0.0;
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(itemDto -> {
            Product product = productDao.getById(itemDto.getProductId());
            if (product == null || product.getStock() < itemDto.getQuantity()) {
                throw new NotEnoughInventoryException("Not enough inventory for product id: " + itemDto.getProductId());
            }
            // Deduct stock and update product
            product.setStock(product.getStock() - itemDto.getQuantity());
            productDao.update(product);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(product.getProductName()); // Snapshot of product name
            orderItem.setPriceAtPurchase(product.getRetailPrice()); // Snapshot price
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setProduct(product); // Set product relationship
            orderItem.setOrder(order);    // Set parent order
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        for (var item : orderItems) {
            amount += item.getPriceAtPurchase() * item.getQuantity();
        }
        order.setTotalAmount(amount);
        orderDao.save(order);
    }

    @Override
    public List<OrderDto> getAllOrdersForUser() {
        User user = getLoggedInUser();
        List<Order> orders = orderDao.getOrdersByUser(user.getUsername());
        if (orders == null || orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user: " + user.getUsername());
        }
        return orders.stream().map(o -> {
            OrderDto dto = new OrderDto();
            dto.setOrderId(o.getId());
            dto.setOrderDate(o.getOrderDate());
            dto.setStatus(o.getStatus());
            dto.setTotalPrice(o.getTotalAmount());
            List<OrderItemDto> items = o.getItems().stream().map(item -> {
                OrderItemDto itemDto = new OrderItemDto();
                itemDto.setProductId(item.getProduct().getId());
                itemDto.setProductName(item.getProductName());
                itemDto.setPriceAtPurchase(item.getPriceAtPurchase());
                itemDto.setQuantity(item.getQuantity());
                return itemDto;
            }).collect(Collectors.toList());
            dto.setItems(items);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderDetail(Long orderId) {
        Order order = orderDao.getById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found");
        }
        OrderDto dto = new OrderDto();
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        List<OrderItemDto> items = order.getItems().stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProductName());
            itemDto.setPriceAtPurchase(item.getPriceAtPurchase());
            itemDto.setQuantity(item.getQuantity());
            return itemDto;
        }).collect(Collectors.toList());
        dto.setItems(items);
        dto.setTotalPrice(order.getTotalAmount());
        return dto;
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderDao.getById(orderId);
        User user = getLoggedInUser();
        if (!user.getId().equals(order.getUser().getId())) {
            throw new RuntimeException("You cannot cancel this order");
        }

        // order status
        if ("Canceled".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Order has already been canceled");
        }
        if ("Completed".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Completed Order cannot be canceled");
        }
        // Restore stock for each order item
        order.getItems().forEach(item -> {
            Product product = productDao.getById(item.getProduct().getId());
            product.setStock(product.getStock() + item.getQuantity());
            productDao.update(product);
        });
        order.setStatus("Canceled");
        orderDao.update(order);
    }
}
