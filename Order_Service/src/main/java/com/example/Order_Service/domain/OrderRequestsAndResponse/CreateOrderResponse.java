package com.example.Order_Service.domain.OrderRequestsAndResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderResponse {
    private Long id;
    private String date;
    private Long user_id;
    private long price;
    // New field for returning the associated product IDs (or details)
    private List<Long> productIds;
}
