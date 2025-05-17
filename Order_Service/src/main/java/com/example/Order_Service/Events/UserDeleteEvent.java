package com.example.Order_Service.Events;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeleteEvent {
    private Long userId;
}
