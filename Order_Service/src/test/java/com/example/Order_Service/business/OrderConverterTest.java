package com.example.Order_Service.business;

import com.example.Order_Service.domain.Order;
import com.example.Order_Service.repository.OrderEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderConverterTest {
    @Test
    void shouldConvertOrderEntityToOrder(){
        LocalDateTime date = LocalDateTime.now();
        OrderEntity order = OrderEntity.builder()
                .id(21L)
                .price(100)
                .userId(2L)
                .date(date)
                .build();

        Order actualOrder = OrderConverter.convert(order);

        Order expectedOrder = Order.builder()
                .id(21L)
                .price(100)
                .user_id(2L)
                .date(date)
                .build();


        assertEquals(expectedOrder, actualOrder);
    }
}
