package com.example.Order_Service.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
//    private Product tickets;
//    private User user;
    private String date;
    private long quantity;
    private long price;
}
