package com.example.Order_Service.business.Impl;

import com.example.Order_Service.business.ICreateOrder;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderRequest;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderResponse;
import com.example.Order_Service.messageBrocker.ProductUpdateEvent;
import com.example.Order_Service.messageBrocker.RabbitMQConfig;
import com.example.Order_Service.repository.OrderEntity;
import com.example.Order_Service.repository.OrderProductEntity;
import com.example.Order_Service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CreateOrderImpl implements ICreateOrder {
    private OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest orderRequest) {
        OrderEntity orderEntity = saveOrder(orderRequest);
        // Extract product IDs from saved order products
        List<String> productIds = orderEntity.getOrderProducts()
                .stream()
                .map(op -> (String) op.getProductId())
                .collect(Collectors.toList());

        Map<String,Integer> qtyMap = orderRequest.getProductIds().stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.summingInt(id -> 1)
                ));

        // publish to product.update queue
        ProductUpdateEvent evt = ProductUpdateEvent.builder()
                .quantities(qtyMap)
                .build();
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EXCHANGE,
                RabbitMQConfig.PRODUCT_ROUTING_KEY,
                evt
        );

        return CreateOrderResponse.builder()
                .id(orderEntity.getId())
                .date(orderEntity.getDate())
                .user_id(orderEntity.getUserId())
                .price(orderEntity.getPrice())
                .productIds(productIds)
                .build();
    }


    private OrderEntity saveOrder(CreateOrderRequest orderRequest){
        // Create the order entity first
        OrderEntity order = OrderEntity.builder()
                .date(orderRequest.getDate())
                .userId(orderRequest.getUser_id())
                .price(orderRequest.getPrice())
                .build();

        // If product IDs are provided, map them to order products and set the relationship
        if (orderRequest.getProductIds() != null && !orderRequest.getProductIds().isEmpty()) {
            List<OrderProductEntity> orderProducts = orderRequest.getProductIds()
                    .stream()
                    .map(productId -> OrderProductEntity.builder()
                            .productId(productId)  // adjust type conversion if needed
                            .order(order)  // set the parent reference
                            .build())
                    .collect(Collectors.toList());
            order.setOrderProducts(orderProducts);
        }

        // Save the order. Cascade settings on the entity will handle the order products.
        return orderRepository.save(order);
    }
}
