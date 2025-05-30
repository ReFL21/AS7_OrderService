package com.example.Order_Service.controller;
import com.example.Order_Service.business.*;
import com.example.Order_Service.domain.Order;
import com.example.Order_Service.domain.OrderRequestsAndResponse.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IGetAllOrders getAllOrders;

    @MockitoBean
    private IGetOrdersByUserId getOrdersByUserId;

    @MockitoBean
    private ICreateOrder createOrder;

    @MockitoBean
    private IDeleteOrder deleteOrder;

    @Test
    @DisplayName("GET /orders returns list of orders")
    void testGetAllOrders() throws Exception {
        List<Order> orders = new ArrayList<>() ;
        orders.add(Order.builder().id(1L).user_id(5L).build());
        orders.add(Order.builder().id(2L).user_id(6L).build());
        GetAllOrders resp = GetAllOrders.builder()
                .orderList(orders).build();
        given(getAllOrders.getAllOrders()).willReturn(resp);

        mockMvc.perform(get("/orders").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderList[0].id").value(1))
                .andExpect(jsonPath("$.orderList[1].id").value(2));
    }

    @Test
    @DisplayName("POST /orders/registerOrder creates a new order")
    void testCreateOrder() throws Exception {

        LocalDateTime now = LocalDateTime.of(2025, 5, 29, 12, 0);

        // Arrange: fill every @NotNull field
        CreateOrderRequest req = CreateOrderRequest.builder()
                .user_id(5L)
                .productIds(Arrays.asList("asdasd", "qwerty"))
                .price(150L)       // must supply a price
                .date(now)         // must supply a date
                .build();

        // And stub a response with matching fields
        CreateOrderResponse resp = CreateOrderResponse.builder()
                .id(100L)
                .user_id(5L)
                .productIds(req.getProductIds())
                .price(req.getPrice())
                .date(now)
                .build();
        given(createOrder.createOrder(any(CreateOrderRequest.class))).willReturn(resp);

        // Act & Assert
        mockMvc.perform(post("/orders/registerOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.user_id").value(5))
                .andExpect(jsonPath("$.productIds").isArray())
                .andExpect(jsonPath("$.price").value(150))
                .andExpect(jsonPath("$.date").exists());
    }

    @Test
    @DisplayName("POST /orders/registerOrder returns 400 Bad Request for invalid input")
    void testCreateOrderInvalidInput() throws Exception {
        // Missing userId
        CreateOrderRequest invalid = CreateOrderRequest.builder().build();
        mockMvc.perform(post("/orders/registerOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /orders/{id} returns orders for user when found")
    void testGetOrdersByUserIdFound() throws Exception {
        List<Order> orders = new ArrayList<>() ;
        orders.add(Order.builder().id(1L).user_id(7L).build());
        GetOrdersByUserIdResponse resp = GetOrdersByUserIdResponse.builder()
                .orders(orders).build();
        given(getOrdersByUserId.getOrdersByUserId(7L)).willReturn(resp);

        mockMvc.perform(get("/orders/7").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.orders[0].userId").value(7))
                .andExpect(jsonPath("$.orders[0].id").value(1));
    }

    @Test
    @DisplayName("GET /orders/{id} with non-numeric ID returns 400 Bad Request")
    void testGetOrdersByUserIdInvalidId() throws Exception {
        mockMvc.perform(get("/orders/abc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /orders/{id} deletes order and returns 204")
    void testDeleteOrder() throws Exception {
        doNothing().when(deleteOrder).deleteOrder(8L);
        mockMvc.perform(delete("/orders/8"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /orders/{id} with non-numeric ID returns 400 Bad Request")
    void testDeleteOrderInvalidId() throws Exception {
        mockMvc.perform(delete("/orders/xyz"))
                .andExpect(status().isBadRequest());
    }
}
