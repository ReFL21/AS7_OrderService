package com.example.Order_Service.Events;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.Order_Service.repository.OrderEntity;
import com.example.Order_Service.repository.OrderRepository;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserDeletingListener.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class UserDeletingListenerDiffblueTest {
    @MockitoBean
    private OrderRepository orderRepository;

    @Autowired
    private UserDeletingListener userDeletingListener;


    @Test
    @DisplayName("Test handleUserDeletion(UserDeleteEvent); then calls deleteAll(Iterable)")
    @Tag("MaintainedByDiffblue")
    void testHandleUserDeletion_thenCallsDeleteAll() {
        // Arrange
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        orderEntity.setId(1L);
        orderEntity.setOrderProducts(new ArrayList<>());
        orderEntity.setPrice(1L);
        orderEntity.setUserId(1L);

        ArrayList<OrderEntity> orderEntityList = new ArrayList<>();
        orderEntityList.add(orderEntity);
        doNothing().when(orderRepository).deleteAll(Mockito.<Iterable<OrderEntity>>any());
        when(orderRepository.findOrderEntitiesByUserId(Mockito.<Long>any())).thenReturn(orderEntityList);

        // Act
        userDeletingListener.handleUserDeletion(new UserDeleteEvent());

        // Assert
        verify(orderRepository).findOrderEntitiesByUserId(isNull());
        verify(orderRepository).deleteAll(isA(Iterable.class));
    }


    @Test
    @DisplayName("Test handleUserDeletion(UserDeleteEvent); when UserDeleteEvent(); then calls findOrderEntitiesByUserId(Long)")
    @Tag("MaintainedByDiffblue")
    void testHandleUserDeletion_whenUserDeleteEvent_thenCallsFindOrderEntitiesByUserId() {
        // Arrange
        when(orderRepository.findOrderEntitiesByUserId(Mockito.<Long>any())).thenReturn(new ArrayList<>());

        // Act
        userDeletingListener.handleUserDeletion(new UserDeleteEvent());

        // Assert
        verify(orderRepository).findOrderEntitiesByUserId(isNull());
    }
}
