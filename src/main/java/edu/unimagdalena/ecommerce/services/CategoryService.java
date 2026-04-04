package edu.unimagdalena.ecommerce.services;


import edu.unimagdalena.ecommerce.api.dto.CategoryDtos.CategoryResponse;
import edu.unimagdalena.ecommerce.api.dto.CategoryDtos.CreateCategoryRequest;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryResponse create(CreateCategoryRequest req);
    CategoryResponse get(UUID id);
    List<CategoryResponse> list();
    void delete(UUID id);
}