package com.example.Order_Service.Events;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.Order_Service.business.ICreateOrder;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderRequest;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderResponse;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderResponse.CreateOrderResponseBuilder;
import com.example.Order_Service.messageBrocker.OrderCreateEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
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

@ContextConfiguration(classes = {OrderEventListener.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class OrderEventListenerDiffblueTest {
    @MockitoBean
    private ICreateOrder iCreateOrder;

    @Autowired
    private OrderEventListener orderEventListener;


    @Test
    @DisplayName("Test onPaymentResult(OrderCreateEvent); then calls createOrder(CreateOrderRequest)")
    @Tag("MaintainedByDiffblue")
    void testOnPaymentResult_thenCallsCreateOrder() {
        // Arrange
        CreateOrderResponseBuilder builderResult = CreateOrderResponse.builder();
        CreateOrderResponseBuilder priceResult = builderResult.date(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L)
                .price(1L);
        CreateOrderResponse buildResult = priceResult.productIds(new ArrayList<>()).user_id(1L).build();
        when(iCreateOrder.createOrder(Mockito.<CreateOrderRequest>any())).thenReturn(buildResult);
        Instant date = LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant();

        // Act
        orderEventListener.onPaymentResult(new OrderCreateEvent(true, date, 1L, 3L, new ArrayList<>()));

        // Assert
        verify(iCreateOrder).createOrder(isA(CreateOrderRequest.class));
    }
}
