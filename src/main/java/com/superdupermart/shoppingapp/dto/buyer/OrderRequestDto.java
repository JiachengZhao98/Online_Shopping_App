package com.superdupermart.shoppingapp.dto.buyer;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class OrderRequestDto {
    @NotEmpty
    private List<OrderItemDto> items;

    // Getters and setters...
    public List<OrderItemDto> getItems() { return items; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }
}
