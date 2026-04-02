package edu.unimagdalena.ecommerce.repositories;


import edu.unimagdalena.ecommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    // Buscar producto por SKU [cite: 129]
    Optional<Product> findBySku(String sku);

    // Buscar productos activos por categoría [cite: 130]
    List<Product> findByCategoryIdAndActiveTrue(UUID categoryId);
}