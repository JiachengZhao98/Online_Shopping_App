package com.superdupermart.shoppingapp.dto.seller;

public class SellerSummaryDto {
    private String topProfitProduct;
    private String topPopularProducts; // comma-separated list or similar
    private int totalItemsSold;

    // Getters and setters...
    public String getTopProfitProduct() { return topProfitProduct; }
    public void setTopProfitProduct(String topProfitProduct) { this.topProfitProduct = topProfitProduct; }

    public String getTopPopularProducts() { return topPopularProducts; }
    public void setTopPopularProducts(String topPopularProducts) { this.topPopularProducts = topPopularProducts; }

    public int getTotalItemsSold() { return totalItemsSold; }
    public void setTotalItemsSold(int totalItemsSold) { this.totalItemsSold = totalItemsSold; }
}
