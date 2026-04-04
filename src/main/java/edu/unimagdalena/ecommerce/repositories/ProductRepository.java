package edu.unimagdalena.ecommerce.repositories;


import edu.unimagdalena.ecommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    // Buscar producto por SKU
    Optional<Product> findBySku(String sku);

    // Buscar productos activos por categoría
    List<Product> findByCategoryIdAndActiveTrue(UUID categoryId);
    boolean existsBySku(String sku);
}