package edu.unimagdalena.ecommerce.api.controllers;

import edu.unimagdalena.ecommerce.api.dto.CategoryDtos;
import edu.unimagdalena.ecommerce.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDtos.CategoryResponse> create(
            @Valid @RequestBody CategoryDtos.CreateCategoryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDtos.CategoryResponse>> list() {
        return ResponseEntity.ok(categoryService.list());
    }
}
