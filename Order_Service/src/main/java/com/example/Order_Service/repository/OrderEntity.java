package com.example.Order_Service.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @JoinColumn(name = "ticket_id")
//    @ManyToOne
//    private ProductEntity tickets;
//
//    @JoinColumn(name = "user_id")
//    @ManyToOne
//    private UserEntity user;

    @NotBlank
    @Length(max = 45)
    @Column(name = "date")
    private String date;


    @Column(name = "quantity")
    @NotNull
    private long quantity;

    @Column(name = "price")
    @NotNull
    private long price;

}
