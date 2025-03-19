package com.example.Order_Service.business.Impl;

import com.example.Order_Service.business.ICreateOrder;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderRequest;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderResponse;
import com.example.Order_Service.repository.OrderEntity;
import com.example.Order_Service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreateOrderImpl implements ICreateOrder {
    private OrderRepository orderRepository;
    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest orderRequest) {
        OrderEntity orderEntity = saveOrder(orderRequest);
        return CreateOrderResponse.builder()
                .id(orderEntity.getId())
                .build();
    }


    private OrderEntity saveOrder(CreateOrderRequest orderRequest){
        OrderEntity order = OrderEntity.builder()
//                .user(UserEntity.builder().id(orderRequest.getUser().getId()).build())
//                .tickets(TicketEntity.builder().id(orderRequest.getTicket().getId()).build())
                .date(orderRequest.getDate())
                .quantity(orderRequest.getQuantity())
//                .price(orderRequest.getQuantity()*orderRequest.getTicket().getPrice())
                .build();
        return orderRepository.save(order);
    }
}
