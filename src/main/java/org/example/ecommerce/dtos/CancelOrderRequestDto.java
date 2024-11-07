package org.example.ecommerce.dtos;

import lombok.Data;

@Data
public class CancelOrderRequestDto {
    private int orderId;
    private int userId;
}
