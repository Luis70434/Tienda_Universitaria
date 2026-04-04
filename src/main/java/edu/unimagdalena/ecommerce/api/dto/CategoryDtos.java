package edu.unimagdalena.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.UUID;

public class CategoryDtos {
    public record CreateCategoryRequest(
            @NotBlank String name,
            String description
    ) implements Serializable {}

    public record CategoryResponse(
            UUID id,
            String name,
            String description
    ) implements Serializable {}
}
