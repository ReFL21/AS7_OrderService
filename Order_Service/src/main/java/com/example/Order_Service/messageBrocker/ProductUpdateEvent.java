package com.example.Order_Service.messageBrocker;

import java.util.Map;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateEvent {
    private Map<String, Integer> quantities;

}
