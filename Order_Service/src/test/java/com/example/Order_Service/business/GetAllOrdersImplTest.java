package com.example.Order_Service.business;
import com.example.Order_Service.business.Impl.GetAllOrdersImpl;
import com.example.Order_Service.domain.OrderRequestsAndResponse.GetAllOrders;
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
public class GetAllOrdersImplTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetAllOrdersImpl getAllOrdersImpl;

    @Test
    void getAllOrders_withNoOrders_shouldReturnEmptyList() {
        when(orderRepository.findAll()).thenReturn(List.of());

        GetAllOrders resp = getAllOrdersImpl.getAllOrders();

        assertNotNull(resp);
        assertTrue(resp.getOrderList().isEmpty(), "Order list should be empty");
        verify(orderRepository).findAll();
    }

    @Test
    void getAllOrders_withSomeOrders_shouldReturnSameSizeList() {
        OrderEntity e1 = OrderEntity.builder().id(1L).build();
        OrderEntity e2 = OrderEntity.builder().id(2L).build();
        when(orderRepository.findAll()).thenReturn(List.of(e1, e2));

        GetAllOrders resp = getAllOrdersImpl.getAllOrders();

        assertNotNull(resp);
        assertEquals(2, resp.getOrderList().size(), "Should return two orders");
        verify(orderRepository).findAll();
    }
}
