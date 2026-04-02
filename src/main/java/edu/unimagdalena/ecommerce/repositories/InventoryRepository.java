package edu.unimagdalena.ecommerce.repositories;

import edu.unimagdalena.ecommerce.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    // Productos con stock insuficiente respecto al mínimo
    @Query("SELECT i FROM Inventory i JOIN FETCH i.product WHERE i.availableQuantity < i.minimumStock")
    List<Inventory> findProductsWithInsufficientStock();
}