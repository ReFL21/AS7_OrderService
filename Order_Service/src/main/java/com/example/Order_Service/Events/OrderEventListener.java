package com.example.Order_Service.Events;

import com.example.Order_Service.business.ICreateOrder;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderRequest;
import com.example.Order_Service.messageBrocker.OrderCreateEvent;
import com.example.Order_Service.messageBrocker.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class OrderEventListener {

    @Autowired
    private ICreateOrder createOrder;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void onPaymentResult(OrderCreateEvent event) {
        if (!event.isSuccess()) {
            return;
        }
        Instant inst = event.getDate();
        LocalDateTime date = LocalDateTime.ofInstant(inst, ZoneOffset.UTC);
        CreateOrderRequest req = CreateOrderRequest.builder()
                .date     (date)
                .user_id  ( event.getUserId() )
                .price    ( event.getPrice() )
                .productIds( event.getProductIds() )
                .build();

        createOrder.createOrder(req);
    }


}
