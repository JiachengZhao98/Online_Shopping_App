package com.superdupermart.shoppingapp.service.buyer;

import com.superdupermart.shoppingapp.dto.buyer.ProductDto;
import com.superdupermart.shoppingapp.dto.buyer.ProductDetailDto;
import java.util.List;

public interface ProductService {
    List<ProductDto> getAllInStockProducts();
    ProductDetailDto getProductDetail(Long id);
}
