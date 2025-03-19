package com.example.Order_Service.repository;

import com.example.Order_Service.domain.Order;
import com.example.Order_Service.domain.OrderRequestsAndResponse.GetAllOrders;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
        List<OrderEntity> findOrderEntitiesByUserId(Long user_id);
}
