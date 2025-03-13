package com.superdupermart.shoppingapp.controller.buyer;

import com.superdupermart.shoppingapp.dto.buyer.ProductDto;
import com.superdupermart.shoppingapp.dto.buyer.ProductDetailDto;
import com.superdupermart.shoppingapp.service.buyer.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/buyer/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // List only in-stock products
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllInStockProducts() {
        List<ProductDto> products = productService.getAllInStockProducts();
        return ResponseEntity.ok(products);
    }

    // Get detailed info (without stock quantity)
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDto> getProductDetail(@PathVariable Long id) {
        ProductDetailDto detail = productService.getProductDetail(id);
        return ResponseEntity.ok(detail);
    }
}
