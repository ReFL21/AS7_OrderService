package com.example.Order_Service.business;

import com.example.Order_Service.domain.Order;
import com.example.Order_Service.domain.OrderRequestsAndResponse.GetAllOrders;
import com.example.Order_Service.repository.OrderRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public class GetAllOrdersImpl implements IGetAllOrders {
    private OrderRepository orderRepository;

    @Transactional
    public GetAllOrders getAllOrders(){
        List<Order> orderEntities;
        orderEntities = orderRepository.findAll()
                .stream()
                .map(OrderConverter::convert)
                .toList();

        return GetAllOrders.builder()
                .orderList(orderEntities)
                .build();
    }
}
