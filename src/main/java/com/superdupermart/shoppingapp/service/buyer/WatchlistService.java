package com.superdupermart.shoppingapp.service.buyer;

import com.superdupermart.shoppingapp.dto.buyer.WatchlistRequestDto;
import com.superdupermart.shoppingapp.dto.buyer.ProductDto;
import java.util.List;

public interface WatchlistService {
    void addProductToWatchlist(WatchlistRequestDto request);
    void removeProductFromWatchlist(Long productId);
    List<ProductDto> getWatchlistProducts();
}
