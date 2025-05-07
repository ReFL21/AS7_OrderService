package com.example.Order_Service.business;
import com.example.Order_Service.business.Impl.DeleteOrderImpl;
import com.example.Order_Service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteOrderImplTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DeleteOrderImpl deleteOrderImpl;

    @Test
    void deleteOrder_shouldInvokeRepositoryDeleteById() {
        Long someId = 77L;

        deleteOrderImpl.deleteOrder(someId);

        verify(orderRepository).deleteById(someId);
    }
}
