package com.example.Order_Service.business;

import com.example.Order_Service.domain.OrderRequestsAndResponse.GetOrdersByUserIdResponse;

public interface IGetOrdersByUserId {
    GetOrdersByUserIdResponse getOrdersByUserId(Long id);
}
