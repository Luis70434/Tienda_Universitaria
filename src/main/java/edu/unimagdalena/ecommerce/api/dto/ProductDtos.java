package edu.unimagdalena.ecommerce.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class ProductDtos {
    public record CreateProductRequest(
            @NotBlank String sku,
            @NotBlank String name,
            String description,
            @NotNull @DecimalMin(value = "0.01") BigDecimal price,
            @NotNull UUID categoryId
    ) implements Serializable {}

    public record UpdateProductRequest(
            @NotBlank String name,
            String description,
            @NotNull @DecimalMin(value = "0.01") BigDecimal price,
            @NotNull Boolean active,
            @NotNull UUID categoryId
    ) implements Serializable {}

    public record ProductResponse(
            UUID id,
            String sku,
            String name,
            String description,
            BigDecimal price,
            Boolean active,
            UUID categoryId,
            String categoryName
    ) implements Serializable {}
}
