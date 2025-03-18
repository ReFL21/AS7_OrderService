package com.example.Order_Service.repository;

import com.example.Order_Service.domain.Order;
import com.example.Order_Service.domain.OrderRequestsAndResponse.GetAllOrders;
import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.List;

public interface OrderRepository {

    List<OrderEntity> findAll();
}
