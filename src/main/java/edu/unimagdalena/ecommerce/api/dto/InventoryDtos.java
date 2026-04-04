package edu.unimagdalena.ecommerce.api.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

public class InventoryDtos {
    public record UpdateInventoryRequest(
            @NotNull(message = "La cantidad disponible es obligatoria")
            @Min(value = 0, message = "El inventario disponible no puede ser negativo")
            Integer availableQuantity,

            @NotNull(message = "El stock mínimo es obligatorio")
            @Min(value = 0, message = "El stock mínimo no puede ser negativo")
            Integer minimumStock
    ) implements Serializable {}

    public record InventoryResponse(
            UUID id,
            UUID productId,
            Integer availableQuantity,
            Integer minimumStock
    ) implements Serializable {}
}