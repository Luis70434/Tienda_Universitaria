package edu.unimagdalena.ecommerce.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;


public class ProductDtos {

    public record CreateProductRequest(
            @NotBlank(message = "El SKU es obligatorio")
            String sku,

            @NotBlank(message = "El nombre es obligatorio")
            String name,

            String description,

            @NotNull(message = "El precio no puede ser nulo")
            @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
            BigDecimal price,

            @NotNull(message = "La categoría es obligatoria")
            UUID categoryId,

            @NotNull(message = "El stock inicial es obligatorio")
            @Min(value = 0, message = "El inventario no puede ser negativo")
            Integer stock,      //  Inventario disponible

            @NotNull(message = "El stock mínimo es obligatorio")
            @Min(value = 0, message = "El stock mínimo no puede ser negativo")
            Integer minStock    //  Stock mínimo
    ) implements Serializable {}

    public record UpdateProductRequest(
            @NotBlank(message = "El nombre es obligatorio")
            String name,

            String description,

            @NotNull(message = "El precio no puede ser nulo")
            @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
            BigDecimal price,

            @NotNull(message = "El estado activo es obligatorio")
            Boolean active,

            @NotNull(message = "La categoría es obligatoria")
            UUID categoryId
    ) implements Serializable {}

    public record ProductResponse(
            UUID id,
            String sku,
            String name,
            String description,
            BigDecimal price,
            Boolean active,
            UUID categoryId,
            String categoryName,
            Integer stock //
    ) implements Serializable {}
}