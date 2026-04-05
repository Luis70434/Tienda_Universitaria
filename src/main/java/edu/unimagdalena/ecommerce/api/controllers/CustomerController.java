package edu.unimagdalena.ecommerce.api.controllers;

import edu.unimagdalena.ecommerce.api.dto.CustomerDtos;
import edu.unimagdalena.ecommerce.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDtos.CustomerResponse> create(
            @Valid @RequestBody CustomerDtos.CreateCustomerRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDtos.CustomerResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDtos.CustomerResponse>> list() {
        return ResponseEntity.ok(customerService.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDtos.CustomerResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody CustomerDtos.UpdateCustomerRequest request
    ) {
        return ResponseEntity.ok(customerService.update(id, request));
    }
}