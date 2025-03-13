package com.superdupermart.shoppingapp.controller.buyer;

import com.superdupermart.shoppingapp.dto.buyer.ProductDto;
import com.superdupermart.shoppingapp.dto.buyer.WatchlistRequestDto;
import com.superdupermart.shoppingapp.service.buyer.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/buyer/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @PostMapping
    public ResponseEntity<String> addToWatchlist(@RequestBody WatchlistRequestDto request) {
        watchlistService.addProductToWatchlist(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added to watchlist");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeFromWatchlist(@PathVariable Long productId) {
        watchlistService.removeProductFromWatchlist(productId);
        return ResponseEntity.ok("Product removed from watchlist");
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> viewWatchlist() {
        List<ProductDto> products = watchlistService.getWatchlistProducts();
        return ResponseEntity.ok(products);
    }
}
