package com.superdupermart.shoppingapp.controller.seller;

import com.superdupermart.shoppingapp.dto.seller.SellerOrderDto;
import com.superdupermart.shoppingapp.service.seller.SellerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/seller/dashboard")
public class SellerDashboardController {

    @Autowired
    private SellerOrderService sellerOrderService;

    @GetMapping("/orders")
    public ResponseEntity<List<SellerOrderDto>> getDashboardOrders(
            @RequestParam(defaultValue = "0") int page) {
        List<SellerOrderDto> orders = sellerOrderService.getDashboardOrders(page, 20);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<SellerOrderDto> getOrderDetail(@PathVariable Long orderId) {
        SellerOrderDto order = sellerOrderService.getSellerOrderDetail(orderId);
        return ResponseEntity.ok(order);
    }
}
