package edu.unimagdalena.ecommerce.repositories;

import edu.unimagdalena.ecommerce.entities.*;
import edu.unimagdalena.ecommerce.enums.CustomerStatus;
import edu.unimagdalena.ecommerce.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemRepositoryIntegrationTest extends AbstractRepositoryTI{
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("Debe obtener productos más vendidos por período")
    void shouldFindBestSellingProducts() {
        Category category = categoryRepository.save(
                Category.builder()
                        .name("Libros")
                        .description("Categoría libros")
                        .build()
        );

        Product product1 = productRepository.save(
                Product.builder()
                        .sku("SKU-A")
                        .name("Libro A")
                        .description("Libro A")
                        .price(new BigDecimal("50000.00"))
                        .active(true)
                        .category(category)
                        .build()
        );

        Product product2 = productRepository.save(
                Product.builder()
                        .sku("SKU-B")
                        .name("Libro B")
                        .description("Libro B")
                        .price(new BigDecimal("30000.00"))
                        .active(true)
                        .category(category)
                        .build()
        );

        Customer customer = customerRepository.save(
                Customer.builder()
                        .firstName("Pedro")
                        .lastName("Mejia")
                        .email("ventas@test.com")
                        .status(CustomerStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Address address = addressRepository.save(
                Address.builder()
                        .customer(customer)
                        .addressLine("Calle 8")
                        .city("Santa Marta")
                        .postalCode("470005")
                        .build()
        );

        Order order = orderRepository.save(
                Order.builder()
                        .customer(customer)
                        .address(address)
                        .status(OrderStatus.PAID)
                        .totalAmount(new BigDecimal("130000.00"))
                        .createdAt(LocalDateTime.of(2026, 4, 10, 10, 0))
                        .build()
        );

        orderItemRepository.save(
                OrderItem.builder()
                        .order(order)
                        .product(product1)
                        .quantity(2)
                        .unitPrice(new BigDecimal("50000.00"))
                        .subtotal(new BigDecimal("100000.00"))
                        .build()
        );

        orderItemRepository.save(
                OrderItem.builder()
                        .order(order)
                        .product(product2)
                        .quantity(1)
                        .unitPrice(new BigDecimal("30000.00"))
                        .subtotal(new BigDecimal("30000.00"))
                        .build()
        );

        List<Object[]> result = orderItemRepository.findBestSellingProducts(
                LocalDateTime.of(2026, 4, 1, 0, 0),
                LocalDateTime.of(2026, 4, 30, 23, 59)
        );

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)[0]).isEqualTo(product1.getId());
        assertThat(result.get(0)[1]).isEqualTo("Libro A");
        assertThat(((Number) result.get(0)[2]).longValue()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Debe obtener top de categorías por volumen de ventas")
    void shouldFindTopCategoriesBySales() {
        Category category = categoryRepository.save(
                Category.builder()
                        .name("Tecnologia")
                        .description("Tecnologia")
                        .build()
        );

        Product product = productRepository.save(
                Product.builder()
                        .sku("SKU-T1")
                        .name("Mouse")
                        .description("Mouse USB")
                        .price(new BigDecimal("40000.00"))
                        .active(true)
                        .category(category)
                        .build()
        );

        Customer customer = customerRepository.save(
                Customer.builder()
                        .firstName("Sara")
                        .lastName("Lopez")
                        .email("sara@test.com")
                        .status(CustomerStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Address address = addressRepository.save(
                Address.builder()
                        .customer(customer)
                        .addressLine("Carrera 10")
                        .city("Santa Marta")
                        .postalCode("470006")
                        .build()
        );

        Order order = orderRepository.save(
                Order.builder()
                        .customer(customer)
                        .address(address)
                        .status(OrderStatus.DELIVERED)
                        .totalAmount(new BigDecimal("120000.00"))
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        orderItemRepository.save(
                OrderItem.builder()
                        .order(order)
                        .product(product)
                        .quantity(3)
                        .unitPrice(new BigDecimal("40000.00"))
                        .subtotal(new BigDecimal("120000.00"))
                        .build()
        );

        List<Object[]> result = orderItemRepository.findTopCategoriesBySales();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)[0]).isEqualTo("Tecnologia");
        assertThat(((Number) result.get(0)[1]).longValue()).isEqualTo(3L);
    }
}
