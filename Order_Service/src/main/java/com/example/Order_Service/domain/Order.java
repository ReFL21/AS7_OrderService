package com.example.Order_Service.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private String date;
    private Long user_id;
    private long price;
    private List<OrderProducts> orderProductIds;
}
