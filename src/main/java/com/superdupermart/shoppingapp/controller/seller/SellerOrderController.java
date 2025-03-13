package com.superdupermart.shoppingapp.controller.seller;

import com.superdupermart.shoppingapp.service.seller.SellerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    @Autowired
    private SellerOrderService sellerOrderService;

    @PatchMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestParam Long orderId) {
        sellerOrderService.completeOrder(orderId);
        return ResponseEntity.ok("Order completed successfully");
    }

    @PatchMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@RequestParam Long orderId) {
        sellerOrderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order canceled successfully");
    }
}
