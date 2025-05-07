package com.example.Order_Service.business;

import com.example.Order_Service.domain.Order;
import com.example.Order_Service.repository.OrderEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderConverterTest {
    @Test
    void shouldConvertOrderEntityToOrder(){
        OrderEntity order = OrderEntity.builder()
                .id(21L)
                .price(100)
//                .user(UserEntity.builder().id(1L).build())
//                .tickets(TicketEntity.builder().id(1L).match(FootballMatchEntity.builder().id(1l).build()).build())
                .userId(2L)
                .date(LocalDateTime.now())
                .build();

        Order actualOrder = OrderConverter.convert(order);

        Order expectedOrder = Order.builder()
                .id(21L)
                .price(100)
//                .user(User.builder().id(1L).build())
//                .tickets(Tickets.builder().id(1L).match(FootballMatch.builder().id(1l).build()).build())
                .user_id(2L)
                .date(LocalDateTime.now())
                .build();


        assertEquals(expectedOrder, actualOrder);
    }
}
