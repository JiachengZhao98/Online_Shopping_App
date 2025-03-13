package com.superdupermart.shoppingapp.dto.seller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;

public class SellerProductDto {

    @NotBlank
    private String name;

    private String description;

    @Min(0)
    private double wholesalePrice;

    @Min(0)
    private double retailPrice;

    @Min(0)
    private int stock;

    // Getters and setters...

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getWholesalePrice() { return wholesalePrice; }
    public void setWholesalePrice(double wholesalePrice) { this.wholesalePrice = wholesalePrice; }

    public double getRetailPrice() { return retailPrice; }
    public void setRetailPrice(double retailPrice) { this.retailPrice = retailPrice; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
