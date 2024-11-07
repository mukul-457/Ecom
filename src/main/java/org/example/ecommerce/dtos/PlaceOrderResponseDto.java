package org.example.ecommerce.dtos;

import lombok.Data;
import org.example.ecommerce.models.Order;

@Data
public class PlaceOrderResponseDto {
    private Order order;
    private ResponseStatus status;
}

