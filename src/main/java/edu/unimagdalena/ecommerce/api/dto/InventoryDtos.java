package edu.unimagdalena.ecommerce.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public class InventoryDtos {
    public record UpdateInventoryRequest(
            @NotNull @Min(0) Integer availableQuantity,
            @NotNull @Min(0) Integer minimumStock
    ) implements Serializable {}

    public record InventoryResponse(
            UUID id,
            UUID productId,
            Integer availableQuantity,
            Integer minimumStock
    ) implements Serializable {}
}
