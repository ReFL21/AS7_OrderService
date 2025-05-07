package com.example.Order_Service.business;
import com.example.Order_Service.business.Impl.GetOrderByUserIdImpl;
import com.example.Order_Service.domain.OrderRequestsAndResponse.GetOrdersByUserIdResponse;
import com.example.Order_Service.repository.OrderEntity;
import com.example.Order_Service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetOrdersByUserIdImplTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetOrderByUserIdImpl getOrderByUserIdImpl;

    @Test
    void getOrdersByUserId_withNone_shouldReturnEmpty() {
        Long userId = 5L;
        when(orderRepository.findOrderEntitiesByUserId(userId)).thenReturn(List.of());

        GetOrdersByUserIdResponse resp = getOrderByUserIdImpl.getOrdersByUserId(userId);

        assertNotNull(resp);
        assertTrue(resp.getOrders().isEmpty(), "Should be no orders for this user");
        verify(orderRepository).findOrderEntitiesByUserId(userId);
    }

    @Test
    void getOrdersByUserId_withSome_shouldReturnSameSizeList() {
        Long userId = 7L;
        OrderEntity e1 = OrderEntity.builder().id(10L).userId(userId).build();
        OrderEntity e2 = OrderEntity.builder().id(11L).userId(userId).build();
        when(orderRepository.findOrderEntitiesByUserId(userId))
                .thenReturn(List.of(e1, e2));

        GetOrdersByUserIdResponse resp = getOrderByUserIdImpl.getOrdersByUserId(userId);

        assertNotNull(resp);
        assertEquals(2, resp.getOrders().size(), "Should return two orders");
        verify(orderRepository).findOrderEntitiesByUserId(userId);
    }
}
