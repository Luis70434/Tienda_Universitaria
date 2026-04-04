package edu.unimagdalena.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

public class AddressDtos {
    public record CreateAddressRequest(
            @NotBlank String street,
            @NotBlank String city,
            @NotBlank String state,
            @NotBlank String zipCode,
            @NotBlank String country,
            @NotNull UUID customerId
    ) implements Serializable {}

    public record AddressResponse(
            UUID id,
            String street,
            String city,
            String state,
            String zipCode,
            String country,
            UUID customerId
    ) implements Serializable {}
}