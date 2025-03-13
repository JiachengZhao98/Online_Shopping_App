package com.superdupermart.shoppingapp.service.buyer;

import com.superdupermart.shoppingapp.dto.buyer.OrderRequestDto;
import com.superdupermart.shoppingapp.dto.buyer.OrderDto;

import java.util.List;

public interface OrderService {
    void placeOrder(OrderRequestDto orderRequest);
    List<OrderDto> getAllOrdersForUser();
    OrderDto getOrderDetail(Long orderId);
    void cancelOrder(Long orderId);
}
