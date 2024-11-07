package org.example.ecommerce.dtos;

import lombok.Data;
import org.example.ecommerce.models.Order;

@Data
public class CancelOrderResponseDto {
    private ResponseStatus status;
    private Order order;
}
