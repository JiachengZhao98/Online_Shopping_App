package com.superdupermart.shoppingapp.dto.buyer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderItemDto {

    @NotNull
    private Long productId;

    @NotNull
    private String productName;

    private double priceAtPurchase;

    @Min(1)
    private int quantity;

    // Getters and setters...

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(double priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }
}
