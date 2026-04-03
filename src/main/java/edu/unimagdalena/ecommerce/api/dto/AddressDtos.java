package edu.unimagdalena.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.UUID;

public class AddressDtos {
    public record CreateAddressRequest(
            @NotBlank String addressLine,
            @NotBlank String city,
            String postalCode
    ) implements Serializable {}

    public record AddressResponse(
            UUID id,
            UUID customerId,
            String addressLine,
            String city,
            String postalCode
    ) implements Serializable {}
}
