package com.superdupermart.shoppingapp.service.buyer;

import com.superdupermart.shoppingapp.dto.buyer.ProductDto;
import java.util.List;

public interface OrderSummaryService {
    List<ProductDto> getTopFrequentProducts();
    List<ProductDto> getTopRecentProducts();
}
