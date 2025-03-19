package com.example.Order_Service.business.Impl;

import com.example.Order_Service.business.IDeleteOrder;
import com.example.Order_Service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeleteOrderImpl implements IDeleteOrder {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        this.orderRepository.deleteById(id);
    }
}
