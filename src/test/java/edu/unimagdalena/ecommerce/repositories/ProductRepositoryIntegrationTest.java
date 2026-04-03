package edu.unimagdalena.ecommerce.repositories;

import edu.unimagdalena.ecommerce.entities.Category;
import edu.unimagdalena.ecommerce.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRepositoryIntegrationTest extends AbstractRepositoryTI{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Debe buscar un producto por SKU")
    void shouldFindProductBySku() {
        Category category = categoryRepository.save(
                Category.builder()
                        .name("Libros")
                        .description("Categoría libros")
                        .build()
        );

        Product product = productRepository.save(
                Product.builder()
                        .sku("SKU-001")
                        .name("Libro Java")
                        .description("Libro de programación")
                        .price(new BigDecimal("120000.00"))
                        .active(true)
                        .category(category)
                        .build()
        );

        Optional<Product> found = productRepository.findBySku("SKU-001");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(product.getId());
        assertThat(found.get().getName()).isEqualTo("Libro Java");
    }

    @Test
    @DisplayName("Debe buscar productos activos por categoría")
    void shouldFindActiveProductsByCategoryId() {
        Category category = categoryRepository.save(
                Category.builder()
                        .name("Accesorios")
                        .description("Accesorios universitarios")
                        .build()
        );

        Product activeProduct = productRepository.save(
                Product.builder()
                        .sku("SKU-002")
                        .name("Mochila")
                        .description("Mochila institucional")
                        .price(new BigDecimal("95000.00"))
                        .active(true)
                        .category(category)
                        .build()
        );

        productRepository.save(
                Product.builder()
                        .sku("SKU-003")
                        .name("Llavero")
                        .description("Llavero institucional")
                        .price(new BigDecimal("15000.00"))
                        .active(false)
                        .category(category)
                        .build()
        );

        List<Product> found = productRepository.findByCategoryIdAndActiveTrue(category.getId());

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getId()).isEqualTo(activeProduct.getId());
        assertThat(found.get(0).getActive()).isTrue();
    }
}
