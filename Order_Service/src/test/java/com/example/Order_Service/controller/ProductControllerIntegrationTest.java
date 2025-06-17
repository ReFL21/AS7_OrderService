package com.example.Order_Service.controller;

import com.example.Order_Service.OrderServiceApplication;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderRequest;
import com.example.Order_Service.messageBrocker.ProductUpdateEvent;
import com.example.Order_Service.messageBrocker.RabbitMQConfig;
import com.example.Order_Service.repository.OrderEntity;
import com.example.Order_Service.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        classes = OrderServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.jpa.show-sql=true"
        }
)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase   // switch to in-memory H2
@Transactional
@Rollback
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    // Replace RabbitTemplate with a Mockito mock so the real service still calls it,
    // but we can capture and assert on the published event.
    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    @DisplayName("GET /orders → 200 + all orders")
    void shouldReturnAllOrders() throws Exception {
        // Given: two orders persisted
        LocalDateTime now = LocalDateTime.now();
        OrderEntity o1 = orderRepository.save(
                OrderEntity.builder()
                        .userId(5L)
                        .price(50L)
                        .date(now)
                        .build()
        );
        OrderEntity o2 = orderRepository.save(
                OrderEntity.builder()
                        .userId(6L)
                        .price(75L)
                        .date(now)
                        .build()
        );

        // When / Then
        mockMvc.perform(get("/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderList.length()").value(2))
                .andExpect(jsonPath("$.orderList[0].id").value(o1.getId()))
                .andExpect(jsonPath("$.orderList[1].id").value(o2.getId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Customer"})
    @DisplayName("POST /orders/registerOrder → 201 + persisted + publishes ProductUpdateEvent")
    void shouldCreateNewOrderAndPublishEvent() throws Exception {
        // Given
        CreateOrderRequest req = CreateOrderRequest.builder()
                .user_id(42L)
                .productIds(List.of("A","A","B"))
                .price(200L)
                .date(LocalDateTime.of(2025,6,17,11,0))
                .build();

        // When / Then HTTP
        mockMvc.perform(post("/orders/registerOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.user_id").value(42));

        // And: verify DB state
        List<OrderEntity> all = orderRepository.findAll();
        assertThat(all).hasSize(1);
        OrderEntity saved = all.get(0);
        assertThat(saved.getUserId()).isEqualTo(42L);
        assertThat(saved.getPrice()).isEqualTo(200L);

        // And: verify messaging
        ArgumentCaptor<ProductUpdateEvent> captor =
                ArgumentCaptor.forClass(ProductUpdateEvent.class);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.PRODUCT_EXCHANGE),
                eq(RabbitMQConfig.PRODUCT_ROUTING_KEY),
                captor.capture()
        );

        Map<String,Integer> qty = captor.getValue().getQuantities();
        assertThat(qty).containsEntry("A", 2)
                .containsEntry("B", 1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    @DisplayName("POST /orders/registerOrder → 400 Bad Request on invalid input")
    void shouldReturn400WhenCreateOrderInvalidInput() throws Exception {
        // Missing all required fields
        CreateOrderRequest invalid = CreateOrderRequest.builder().build();

        mockMvc.perform(post("/orders/registerOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Customer"})
    @DisplayName("GET /orders/{userId} → 200 + orders for user")
    void shouldReturnOrdersForUser() throws Exception {
        // Given: one order for user 7
        LocalDateTime now = LocalDateTime.now();
        OrderEntity oe = orderRepository.save(
                OrderEntity.builder()
                        .userId(7L)
                        .price(30L)
                        .date(now)
                        .build()
        );

        mockMvc.perform(get("/orders/7")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].id").value(oe.getId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    @DisplayName("GET /orders/{id} → 400 Bad Request on non-numeric ID")
    void shouldReturn400WhenGetOrdersByUserIdInvalidId() throws Exception {
        mockMvc.perform(get("/orders/abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    @DisplayName("DELETE /orders/{id} → 204 No Content")
    void shouldDeleteOrder() throws Exception {
        // Given one to delete
        LocalDateTime now = LocalDateTime.now();
        OrderEntity oe = orderRepository.save(
                OrderEntity.builder()
                        .userId(8L)
                        .price(20L)
                        .date(now)
                        .build()
        );

        mockMvc.perform(delete("/orders/" + oe.getId()))
                .andExpect(status().isNoContent());

        assertThat(orderRepository.findById(oe.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    @DisplayName("DELETE /orders/{id} → 400 Bad Request on non-numeric ID")
    void shouldReturn400WhenDeleteOrderInvalidId() throws Exception {
        mockMvc.perform(delete("/orders/xyz"))
                .andExpect(status().isBadRequest());
    }
}
