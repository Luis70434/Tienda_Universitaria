package edu.unimagdalena.ecommerce.services;




import edu.unimagdalena.ecommerce.api.dto.ProductDtos;
import edu.unimagdalena.ecommerce.api.dto.ProductDtos.CreateProductRequest;
import edu.unimagdalena.ecommerce.api.dto.ProductDtos.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponse create(CreateProductRequest req);
    ProductResponse get(UUID id);
    List<ProductResponse> list();
    ProductResponse update(UUID id, ProductDtos.UpdateProductRequest req);
    void delete(UUID id);

    // Métodos adicionales para cumplir la Regla 6.6
    List<ProductResponse> getLowStock();
    List<ProductResponse> getByCategory(UUID categoryId);
}