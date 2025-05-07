package com.example.Order_Service.business;

import com.example.Order_Service.business.Impl.CreateOrderImpl;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderRequest;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderResponse;
import com.example.Order_Service.messageBrocker.ProductUpdateEvent;
import com.example.Order_Service.messageBrocker.RabbitMQConfig;
import com.example.Order_Service.repository.OrderEntity;
import com.example.Order_Service.repository.OrderProductEntity;
import com.example.Order_Service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateOrderImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CreateOrderImpl createOrderImpl;

    @Test
    void createOrder_shouldSaveOrderAndPublishEventAndReturnResponse() {
        // Arrange
        LocalDateTime date = LocalDateTime.now();
        Long userId= 42L;
        long price= 199;
        List<String> products = List.of("p1", "p2", "p1");

        CreateOrderRequest req = CreateOrderRequest.builder()
                .date(date)
                .user_id(userId)
                .price(199)
                .productIds(products)
                .build();

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> {
                    OrderEntity arg = invocation.getArgument(0);
                    arg.setId(100L);
                    return arg;
                });

        // Act
        CreateOrderResponse resp = createOrderImpl.createOrder(req);

        // Assert response fields
        assertNotNull(resp, "Response should not be null");
        assertEquals(100L, resp.getId());
        assertEquals(date,   resp.getDate());
        assertEquals(userId, resp.getUser_id());
        assertEquals(price,  resp.getPrice());
        assertEquals(products, resp.getProductIds());

        // Verify that save(...) was called with an entity having correct products
        ArgumentCaptor<OrderEntity> orderCap = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderRepository).save(orderCap.capture());
        OrderEntity saved = orderCap.getValue();
        List<OrderProductEntity> ops = saved.getOrderProducts();
        assertNotNull(ops);
        assertEquals(3, ops.size());
        assertEquals("p1", ops.get(0).getProductId());
        assertEquals("p2", ops.get(1).getProductId());
        assertEquals("p1", ops.get(2).getProductId());

        // Verify that the correct event was published
        @SuppressWarnings("unchecked")
        ArgumentCaptor<ProductUpdateEvent> evtCap =
                ArgumentCaptor.forClass(ProductUpdateEvent.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.PRODUCT_EXCHANGE),
                eq(RabbitMQConfig.PRODUCT_ROUTING_KEY),
                evtCap.capture()
        );
        ProductUpdateEvent evt = evtCap.getValue();
        Map<String, Integer> expectedQty = Map.of("p1", 2, "p2", 1);
        assertEquals(expectedQty, evt.getQuantities());
    }
}
