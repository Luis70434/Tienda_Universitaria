package edu.unimagdalena.ecommerce.api.controllers;

import edu.unimagdalena.ecommerce.api.dto.OrderDtos;
import edu.unimagdalena.ecommerce.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDtos.OrderResponse> create(
            @Valid @RequestBody OrderDtos.CreateOrderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDtos.OrderResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderDtos.OrderResponse>> list() {
        return ResponseEntity.ok(orderService.list());
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<OrderDtos.OrderResponse> pay(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.pay(id));
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderDtos.OrderResponse> ship(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.ship(id));
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<OrderDtos.OrderResponse> deliver(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.deliver(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderDtos.OrderResponse> cancel(
            @PathVariable UUID id,
            @RequestBody(required = false) OrderDtos.CancelOrderRequest request
    ) {
        OrderDtos.CancelOrderRequest body =
                request != null ? request : new OrderDtos.CancelOrderRequest(null);

        return ResponseEntity.ok(orderService.cancel(id, body));
    }
}
