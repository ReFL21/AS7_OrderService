package com.example.Order_Service.domain.OrderRequestsAndResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotNull
    private List<String> productIds;
    @NotNull
    private long price;
    @NotNull
    private LocalDateTime date;

    @NotNull
    private Long user_id;
}
