package com.superdupermart.shoppingapp.dto.buyer;

import java.util.*;

public class OrderDto {

    private Long orderId;
    private Date orderDate;
    private String status;
    private double totalPrice;
    private List<OrderItemDto> items;

    // Getters and setters...

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderTime) { this.orderDate = orderTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<OrderItemDto> getItems() { return items; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
