package edu.unimagdalena.ecommerce.api.dto;

import edu.unimagdalena.ecommerce.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderDtos {
    public record CreateOrderItemRequest(
            @NotNull UUID productId,
            @NotNull @Min(1) Integer quantity
    ) implements Serializable {}

    public record CreateOrderRequest(
            @NotNull UUID customerId,
            @NotNull UUID addressId,
            @NotEmpty List<@Valid CreateOrderItemRequest> items
    ) implements Serializable {}

    public record CancelOrderRequest(
            String reason
    ) implements Serializable {}

    public record OrderItemResponse(
            UUID id,
            UUID productId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) implements Serializable {}

    public record OrderResponse(
            UUID id,
            UUID customerId,
            UUID addressId,
            OrderStatus status,
            BigDecimal totalAmount,
            LocalDateTime createdAt,
            List<OrderItemResponse> items
    ) implements Serializable {}
}
