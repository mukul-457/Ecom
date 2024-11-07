package org.example.ecommerce.controllers;

import org.example.ecommerce.dtos.*;
import org.example.ecommerce.models.Order;
import org.example.ecommerce.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.example.ecommerce.exceptions.*;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    public PlaceOrderResponseDto placeOrder(PlaceOrderRequestDto placeOrderRequestDto) {
        PlaceOrderResponseDto responseDto = new PlaceOrderResponseDto();
        try{
            Order order = orderService.placeOrder(placeOrderRequestDto.getUserId(), placeOrderRequestDto.getAddressId(),placeOrderRequestDto.getOrderDetails());
            responseDto.setOrder(order);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        }catch(Exception e){
            responseDto.setStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }

    public CancelOrderResponseDto cancelOrder(CancelOrderRequestDto cancelOrderRequestDto) {
        CancelOrderResponseDto responseDto = new CancelOrderResponseDto();
        try{
            Order  order = orderService.cancelOrder(cancelOrderRequestDto.getOrderId(), cancelOrderRequestDto.getUserId());
            responseDto.setOrder(order);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        }catch(UserNotFoundException | OrderNotFoundException | OrderCannotBeCancelledException | OrderDoesNotBelongToUserException e){
            responseDto.setStatus(ResponseStatus.FAILURE);
        }

        return responseDto;
    }
}