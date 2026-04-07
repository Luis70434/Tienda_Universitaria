package edu.unimagdalena.ecommerce.api.controllers;

import edu.unimagdalena.ecommerce.api.dto.AddressDtos;
import edu.unimagdalena.ecommerce.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressDtos.AddressResponse> create(
            @PathVariable UUID customerId,
            @Valid @RequestBody AddressDtos.CreateAddressRequest request
    ) {
        AddressDtos.CreateAddressRequest fixedRequest =
                new AddressDtos.CreateAddressRequest(
                        request.street(),
                        request.city(),
                        request.state(),
                        request.zipCode(),
                        request.country(),
                        customerId
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.create(fixedRequest));
    }

    @GetMapping
    public ResponseEntity<List<AddressDtos.AddressResponse>> listByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(addressService.listByCustomer(customerId));
    }
}