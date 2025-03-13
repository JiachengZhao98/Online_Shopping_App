package com.superdupermart.shoppingapp.dto.buyer;

public class ProductDto {
    private String name;
    private String description;
    private double retailPrice;

    // Getters and setters...

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getRetailPrice() { return retailPrice; }
    public void setRetailPrice(double retailPrice) { this.retailPrice = retailPrice; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
