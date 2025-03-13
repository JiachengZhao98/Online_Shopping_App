package com.superdupermart.shoppingapp.controller.seller;

import com.superdupermart.shoppingapp.dto.seller.SellerProductDto;
import com.superdupermart.shoppingapp.service.seller.SellerProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller/products")
public class SellerProductController {

    @Autowired
    private SellerProductService sellerProductService;

    @GetMapping("/{id}")
    public ResponseEntity<SellerProductDto> getProductDetail(@PathVariable Long id) {
        SellerProductDto dto = sellerProductService.getProductDetail(id);
        return ResponseEntity.ok(dto);
    }

    // get all products
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(sellerProductService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@Validated @RequestBody SellerProductDto productDto) {
        sellerProductService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
    }

    @PatchMapping()
    public ResponseEntity<String> updateProduct(@RequestBody SellerProductDto productDto) {
        sellerProductService.updateProduct(productDto);
        return ResponseEntity.ok("Product updated successfully");
    }
}
