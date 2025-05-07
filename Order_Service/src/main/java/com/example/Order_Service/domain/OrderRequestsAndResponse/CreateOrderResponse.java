package com.example.Order_Service.domain.OrderRequestsAndResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderResponse {
    private Long id;
    private LocalDateTime date;
    private Long user_id;
    private long price;
    private List<String> productIds;
}
