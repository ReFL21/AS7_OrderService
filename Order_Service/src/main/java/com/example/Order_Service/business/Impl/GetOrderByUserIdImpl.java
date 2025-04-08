package com.example.Order_Service.business.Impl;

import com.example.Order_Service.business.IGetAllOrders;
import com.example.Order_Service.business.IGetOrdersByUserId;
import com.example.Order_Service.business.OrderConverter;
import com.example.Order_Service.domain.Order;
import com.example.Order_Service.domain.OrderRequestsAndResponse.GetOrdersByUserIdResponse;
import com.example.Order_Service.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GetOrderByUserIdImpl implements IGetOrdersByUserId {
    private OrderRepository orderRepository;

    @Override
    public GetOrdersByUserIdResponse getOrdersByUserId(Long id) {
        List<Order> orders = orderRepository.findOrderEntitiesByUserId(id)
                .stream()
                .map(OrderConverter::convert)
                .toList();

        return GetOrdersByUserIdResponse.builder()
                .orders(orders)
                .build();
    }
}
