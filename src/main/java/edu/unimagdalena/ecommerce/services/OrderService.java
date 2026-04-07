package edu.unimagdalena.ecommerce.services;


import edu.unimagdalena.ecommerce.api.dto.OrderDtos.*;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse create(CreateOrderRequest req); //
    OrderResponse pay(UUID id);                   //
    OrderResponse ship(UUID id);                  //
    OrderResponse deliver(UUID id);               //
    OrderResponse cancel(UUID id, CancelOrderRequest req); //
    OrderResponse get(UUID id);
    List<OrderResponse> list();
    List<OrderResponse> listByCustomer(UUID customerId);
}