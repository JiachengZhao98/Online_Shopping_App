package com.superdupermart.shoppingapp.controller.buyer;

import com.superdupermart.shoppingapp.dto.buyer.ProductDto;
import com.superdupermart.shoppingapp.service.buyer.OrderSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/buyer/orders/summary")
public class OrderSummaryController {

    @Autowired
    private OrderSummaryService orderSummaryService;

    @GetMapping("/frequent")
    public ResponseEntity<List<ProductDto>> getTopFrequentProducts() {
        List<ProductDto> products = orderSummaryService.getTopFrequentProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ProductDto>> getTopRecentProducts() {
        List<ProductDto> products = orderSummaryService.getTopRecentProducts();
        return ResponseEntity.ok(products);
    }
}
