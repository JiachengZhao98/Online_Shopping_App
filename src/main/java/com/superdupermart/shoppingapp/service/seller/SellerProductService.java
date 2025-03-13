package com.superdupermart.shoppingapp.service.seller;

import java.util.List;

import com.superdupermart.shoppingapp.dto.seller.SellerProductDto;

public interface SellerProductService {
    List<SellerProductDto> getAllProducts();
    SellerProductDto getProductDetail(Long id);
    void addProduct(SellerProductDto productDto);
    void updateProduct(SellerProductDto productDto);
}
