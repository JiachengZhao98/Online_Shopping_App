package com.superdupermart.shoppingapp.service.seller;

import com.superdupermart.shoppingapp.dto.seller.SellerOrderDto;
import java.util.List;

public interface SellerOrderService {
    void completeOrder(Long orderId);
    void cancelOrder(Long orderId);
    List<SellerOrderDto> getDashboardOrders(int page, int size);
    SellerOrderDto getSellerOrderDetail(Long orderId);
}
