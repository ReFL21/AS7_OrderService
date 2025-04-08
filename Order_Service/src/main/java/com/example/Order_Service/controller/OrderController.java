package com.example.Order_Service.controller;

import com.example.Order_Service.business.ICreateOrder;
import com.example.Order_Service.business.IDeleteOrder;
import com.example.Order_Service.business.IGetAllOrders;
import com.example.Order_Service.business.IGetOrdersByUserId;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderRequest;
import com.example.Order_Service.domain.OrderRequestsAndResponse.CreateOrderResponse;
import com.example.Order_Service.domain.OrderRequestsAndResponse.GetAllOrders;
import com.example.Order_Service.domain.OrderRequestsAndResponse.GetOrdersByUserIdResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/orders")
@CrossOrigin(origins = {"http://localhost:8082"})
public class OrderController {
    @Autowired
    private IGetOrdersByUserId getOrdersByUserId;
    @Autowired
    private IGetAllOrders getAllOrders;
    @Autowired
    private ICreateOrder createOrder;
    @Autowired
    private IDeleteOrder deleteOrders;
//
//    @IsAuthenticated
//    @RolesAllowed({"Admin"})
    @GetMapping
    public ResponseEntity<GetAllOrders> getAllOrders(){
        GetAllOrders response = getAllOrders.getAllOrders();
        return ResponseEntity.ok(response);
    }
//
//    @IsAuthenticated
//    @RolesAllowed({"Customer"})
    @PostMapping("/registerOrder")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request){
        CreateOrderResponse response = createOrder.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
//    @RolesAllowed({"Customer"})
    @GetMapping("/{id}")
    public ResponseEntity<GetOrdersByUserIdResponse> getOrdersByUserId(@PathVariable(value = "id") final Long id){
        GetOrdersByUserIdResponse response = getOrdersByUserId.getOrdersByUserId(id);
        return ResponseEntity.ok(response);

    }
//
//    @IsAuthenticated
//    @RolesAllowed({"Admin"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id){
        deleteOrders.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
