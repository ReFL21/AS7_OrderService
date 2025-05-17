package com.example.Order_Service.Events;

import com.example.Order_Service.messageBrocker.RabbitMQConfig;
import com.example.Order_Service.repository.OrderEntity;
import com.example.Order_Service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor

public class UserDeletingListener {

    private final OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.USER_DELETE_QUEUE)
    public void handleUserDeletion(UserDeleteEvent event) {
        Long userId = event.getUserId();

        // 1) load all matching orders as entities
        List<OrderEntity> orders = orderRepository.findOrderEntitiesByUserId(userId);
        if (orders.isEmpty()) {
            return;
        }

        // 2) delete them via JPA – this will cascade to orderProducts
        orderRepository.deleteAll(orders);
    }



}
