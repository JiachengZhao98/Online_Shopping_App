package com.superdupermart.shoppingapp.dto.buyer;

import javax.validation.constraints.NotNull;

public class WatchlistRequestDto {
    @NotNull
    private Long productId;

    // Getter and setter...
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}
