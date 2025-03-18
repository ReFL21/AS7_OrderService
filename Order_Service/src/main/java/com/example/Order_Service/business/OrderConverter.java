package com.example.Order_Service.business;

import com.example.Order_Service.domain.Order;
import com.example.Order_Service.repository.OrderEntity;

public class OrderConverter {
    private OrderConverter(){}
    public static Order convert(OrderEntity order){
        return Order.builder()
                .id(order.getId())
//                .tickets(TicketConverter.convert(order.getTickets()))
//                .user(UserConverter.convert(order.getUser()))
                .date(order.getDate())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .build();
    }
}
