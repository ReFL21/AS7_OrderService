package com.example.Order_Service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.Order_Service.domain.Order;
import com.example.Order_Service.domain.OrderProducts;
import com.example.Order_Service.repository.OrderEntity;
import com.example.Order_Service.repository.OrderProductEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderConverterDiffblueTest {

    @Test
    @DisplayName("Test convert(OrderEntity); given ArrayList(); then return OrderProductIds Empty")
    @Tag("MaintainedByDiffblue")
    void testConvert_givenArrayList_thenReturnOrderProductIdsEmpty() {
        // Arrange
        OrderEntity orderEntity = new OrderEntity();
        LocalDate ofResult = LocalDate.of(1970, 1, 1);
        orderEntity.setDate(ofResult.atStartOfDay());
        orderEntity.setId(1L);
        orderEntity.setOrderProducts(new ArrayList<>());
        orderEntity.setPrice(1L);
        orderEntity.setUserId(1L);

        // Act
        Order actualConvertResult = OrderConverter.convert(orderEntity);

        // Assert
        LocalDateTime date = actualConvertResult.getDate();
        assertEquals("00:00", date.toLocalTime().toString());
        LocalDate toLocalDateResult = date.toLocalDate();
        assertEquals("1970-01-01", toLocalDateResult.toString());
        assertEquals(1L, actualConvertResult.getPrice());
        assertEquals(1L, actualConvertResult.getId().longValue());
        assertEquals(1L, actualConvertResult.getUser_id().longValue());
        assertTrue(actualConvertResult.getOrderProductIds().isEmpty());
        assertSame(ofResult, toLocalDateResult);
    }


    @Test
    @DisplayName("Test convert(OrderEntity); given 'null'; then return OrderProductIds is 'null'")
    @Tag("MaintainedByDiffblue")
    void testConvert_givenNull_thenReturnOrderProductIdsIsNull() {
        // Arrange
        OrderEntity orderEntity = new OrderEntity();
        LocalDate ofResult = LocalDate.of(1970, 1, 1);
        orderEntity.setDate(ofResult.atStartOfDay());
        orderEntity.setId(1L);
        orderEntity.setPrice(1L);
        orderEntity.setUserId(1L);
        orderEntity.setOrderProducts(null);

        // Act
        Order actualConvertResult = OrderConverter.convert(orderEntity);

        // Assert
        LocalDateTime date = actualConvertResult.getDate();
        assertEquals("00:00", date.toLocalTime().toString());
        LocalDate toLocalDateResult = date.toLocalDate();
        assertEquals("1970-01-01", toLocalDateResult.toString());
        assertNull(actualConvertResult.getOrderProductIds());
        assertEquals(1L, actualConvertResult.getPrice());
        assertEquals(1L, actualConvertResult.getId().longValue());
        assertEquals(1L, actualConvertResult.getUser_id().longValue());
        assertSame(ofResult, toLocalDateResult);
    }

    @Test
    @DisplayName("Test convert(OrderEntity); given OrderEntity() Id is two; then return OrderProductIds size is two")
    @Tag("MaintainedByDiffblue")
    void testConvert_givenOrderEntityIdIsTwo_thenReturnOrderProductIdsSizeIsTwo() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        order.setId(1L);
        order.setOrderProducts(new ArrayList<>());
        order.setPrice(1L);
        order.setUserId(1L);

        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setId(1L);
        orderProductEntity.setOrder(order);
        orderProductEntity.setProductId("42");

        OrderEntity order2 = new OrderEntity();
        order2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        order2.setId(2L);
        order2.setOrderProducts(new ArrayList<>());
        order2.setPrice(0L);
        order2.setUserId(2L);

        OrderProductEntity orderProductEntity2 = new OrderProductEntity();
        orderProductEntity2.setId(2L);
        orderProductEntity2.setOrder(order2);
        orderProductEntity2.setProductId("Product Id");

        ArrayList<OrderProductEntity> orderProducts = new ArrayList<>();
        orderProducts.add(orderProductEntity2);
        orderProducts.add(orderProductEntity);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        orderEntity.setId(1L);
        orderEntity.setOrderProducts(orderProducts);
        orderEntity.setPrice(1L);
        orderEntity.setUserId(1L);

        // Act and Assert
        List<OrderProducts> orderProductIds = OrderConverter.convert(orderEntity).getOrderProductIds();
        assertEquals(2, orderProductIds.size());
        OrderProducts getResult = orderProductIds.get(1);
        assertEquals("42", getResult.getProductId());
        OrderProducts getResult2 = orderProductIds.get(0);
        assertEquals("Product Id", getResult2.getProductId());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals(2L, getResult2.getId().longValue());
    }

    @Test
    @DisplayName("Test convert(OrderEntity); then return OrderProductIds size is one")
    @Tag("MaintainedByDiffblue")
    void testConvert_thenReturnOrderProductIdsSizeIsOne() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        order.setId(1L);
        order.setOrderProducts(new ArrayList<>());
        order.setPrice(1L);
        order.setUserId(1L);

        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setId(1L);
        orderProductEntity.setOrder(order);
        orderProductEntity.setProductId("42");

        ArrayList<OrderProductEntity> orderProducts = new ArrayList<>();
        orderProducts.add(orderProductEntity);

        OrderEntity orderEntity = new OrderEntity();
        LocalDate ofResult = LocalDate.of(1970, 1, 1);
        orderEntity.setDate(ofResult.atStartOfDay());
        orderEntity.setId(1L);
        orderEntity.setOrderProducts(orderProducts);
        orderEntity.setPrice(1L);
        orderEntity.setUserId(1L);

        // Act
        Order actualConvertResult = OrderConverter.convert(orderEntity);

        // Assert
        LocalDateTime date = actualConvertResult.getDate();
        assertEquals("00:00", date.toLocalTime().toString());
        LocalDate toLocalDateResult = date.toLocalDate();
        assertEquals("1970-01-01", toLocalDateResult.toString());
        List<OrderProducts> orderProductIds = actualConvertResult.getOrderProductIds();
        assertEquals(1, orderProductIds.size());
        OrderProducts getResult = orderProductIds.get(0);
        assertEquals("42", getResult.getProductId());
        assertEquals(1L, actualConvertResult.getPrice());
        assertEquals(1L, actualConvertResult.getId().longValue());
        assertEquals(1L, actualConvertResult.getUser_id().longValue());
        assertEquals(1L, getResult.getId().longValue());
        assertSame(ofResult, toLocalDateResult);
    }

    @Test
    @DisplayName("Test convertOrderProduct(OrderProductEntity)")
    @Tag("MaintainedByDiffblue")
    void testConvertOrderProduct() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        order.setId(1L);
        order.setOrderProducts(new ArrayList<>());
        order.setPrice(1L);
        order.setUserId(1L);

        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setId(1L);
        orderProductEntity.setOrder(order);
        orderProductEntity.setProductId("42");

        // Act
        OrderProducts actualConvertOrderProductResult = OrderConverter.convertOrderProduct(orderProductEntity);

        // Assert
        assertEquals("42", actualConvertOrderProductResult.getProductId());
        assertEquals(1L, actualConvertOrderProductResult.getId().longValue());
    }
}
