package com.example.Order_Service.business;

import com.example.Order_Service.domain.Order;
import com.example.Order_Service.domain.OrderProducts;
import com.example.Order_Service.repository.OrderEntity;
import com.example.Order_Service.repository.OrderProductEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OrderConverter {
    private OrderConverter(){}
    public static Order convert(OrderEntity orderEntity) {
        List<OrderProducts> orderProducts = null;
        if (orderEntity.getOrderProducts() != null) {
            orderProducts = orderEntity.getOrderProducts()
                    .stream()
                    .map(OrderConverter::convertOrderProduct)
                    .collect(Collectors.toList());
        }
        return Order.builder()
                .id(orderEntity.getId())
                .date(orderEntity.getDate())
                .user_id(orderEntity.getUserId())
                .price(orderEntity.getPrice())
                .orderProductIds(orderProducts)
                .build();
    }


    public static OrderProducts convertOrderProduct(OrderProductEntity orderProductEntity) {
        return OrderProducts.builder()
                .id(orderProductEntity.getId())
                .productId((long) orderProductEntity.getProductId()) // adjust type as needed
                .build();
    }
}
