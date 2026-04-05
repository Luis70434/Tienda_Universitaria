package edu.unimagdalena.ecommerce.api.controllers;

import edu.unimagdalena.ecommerce.api.dto.InventoryDtos;
import edu.unimagdalena.ecommerce.api.dto.ProductDtos;
import edu.unimagdalena.ecommerce.services.InventoryService;
import edu.unimagdalena.ecommerce.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<ProductDtos.ProductResponse> create(
            @Valid @RequestBody ProductDtos.CreateProductRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDtos.ProductResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDtos.ProductResponse>> list() {
        return ResponseEntity.ok(productService.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDtos.ProductResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductDtos.UpdateProductRequest request
    ) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @PutMapping("/{id}/inventory")
    public ResponseEntity<InventoryDtos.InventoryResponse> updateInventory(
            @PathVariable UUID id,
            @Valid @RequestBody InventoryDtos.UpdateInventoryRequest request
    ) {
        return ResponseEntity.ok(inventoryService.update(id, request));
    }
}