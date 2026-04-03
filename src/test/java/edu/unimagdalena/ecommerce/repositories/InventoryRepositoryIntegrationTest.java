package edu.unimagdalena.ecommerce.repositories;

import edu.unimagdalena.ecommerce.entities.Category;
import edu.unimagdalena.ecommerce.entities.Inventory;
import edu.unimagdalena.ecommerce.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryRepositoryIntegrationTest extends AbstractRepositoryTI{
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Debe encontrar productos con stock insuficiente respecto al mínimo")
    void shouldFindProductsWithInsufficientStock() {
        Category category = categoryRepository.save(
                Category.builder()
                        .name("Papelería")
                        .description("Útiles")
                        .build()
        );

        Product lowStockProduct = productRepository.save(
                Product.builder()
                        .sku("SKU-010")
                        .name("Cuaderno")
                        .description("Cuaderno universitario")
                        .price(new BigDecimal("12000.00"))
                        .active(true)
                        .category(category)
                        .build()
        );

        Product enoughStockProduct = productRepository.save(
                Product.builder()
                        .sku("SKU-011")
                        .name("Agenda")
                        .description("Agenda institucional")
                        .price(new BigDecimal("20000.00"))
                        .active(true)
                        .category(category)
                        .build()
        );

        inventoryRepository.save(
                Inventory.builder()
                        .product(lowStockProduct)
                        .availableQuantity(3)
                        .minimumStock(10)
                        .build()
        );

        inventoryRepository.save(
                Inventory.builder()
                        .product(enoughStockProduct)
                        .availableQuantity(20)
                        .minimumStock(5)
                        .build()
        );

        List<Inventory> found = inventoryRepository.findProductsWithInsufficientStock();

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getProduct().getSku()).isEqualTo("SKU-010");
        assertThat(found.get(0).getAvailableQuantity()).isLessThan(found.get(0).getMinimumStock());
    }
}
