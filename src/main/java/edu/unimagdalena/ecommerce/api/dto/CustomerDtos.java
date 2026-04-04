package edu.unimagdalena.ecommerce.api.dto;

import edu.unimagdalena.ecommerce.enums.CustomerStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerDtos {
    public record CreateCustomerRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @NotBlank @Email String email
    ) implements Serializable {}

    public record UpdateCustomerRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @NotBlank @Email String email,
            @NotNull CustomerStatus status
    ) implements Serializable {}

    public record CustomerResponse(
            UUID id,
            String firstName,
            String lastName,
            String email,
            CustomerStatus status,
            LocalDateTime createdAt
    ) implements Serializable {}
}
