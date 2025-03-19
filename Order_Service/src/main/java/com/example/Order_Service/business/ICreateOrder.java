package com.example.Order_Service.business;

import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderRequest;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderResponse;

public interface ICreateOrder {
    CreateOrderResponse createOrder(CreateOrderRequest orderRequest);
}
