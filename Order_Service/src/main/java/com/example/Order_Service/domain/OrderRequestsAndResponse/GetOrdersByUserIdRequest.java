package com.example.Order_Service.domain.OrderRequestsAndResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GetOrdersByUserIdRequest {
    private Long user_id;
}
