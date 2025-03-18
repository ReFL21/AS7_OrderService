package com.example.Order_Service.domain.OrderRequestsAndResponse;

import com.example.Order_Service.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GetAllOrders {
    private List<Order> orderList;
}
