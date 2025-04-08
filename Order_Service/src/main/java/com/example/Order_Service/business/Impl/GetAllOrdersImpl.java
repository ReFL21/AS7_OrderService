package com.example.Order_Service.business.Impl;

import com.example.Order_Service.business.IGetAllOrders;
import com.example.Order_Service.business.OrderConverter;
import com.example.Order_Service.domain.Order;
import com.example.Order_Service.domain.OrderRequestsAndResponse.GetAllOrders;
import com.example.Order_Service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class GetAllOrdersImpl implements IGetAllOrders {
    private OrderRepository orderRepository;

    @Transactional
    public GetAllOrders getAllOrders(){
        List<Order> orderList = orderRepository.findAll()
                .stream()
                .map(OrderConverter::convert)
                .toList();

        return GetAllOrders.builder()
                .orderList(orderList)
                .build();
    }
}
