package edu.unimagdalena.ecommerce.repositories;

import edu.unimagdalena.ecommerce.entities.Address;
import edu.unimagdalena.ecommerce.entities.Customer;
import edu.unimagdalena.ecommerce.entities.Order;
import edu.unimagdalena.ecommerce.enums.CustomerStatus;
import edu.unimagdalena.ecommerce.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderRepositoryIntegrationTest extends AbstractRepositoryTI{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("Debe buscar pedidos por customerId")
    void shouldFindOrdersByCustomerId() {
        Customer customer = customerRepository.save(
                Customer.builder()
                        .firstName("Luis")
                        .lastName("Perez")
                        .email("cliente1@test.com")
                        .status(CustomerStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Address address = addressRepository.save(
                Address.builder()
                        .customer(customer)
                        .addressLine("Calle 1 # 2 - 3")
                        .city("Santa Marta")
                        .postalCode("470001")
                        .build()
        );

        orderRepository.save(
                Order.builder()
                        .customer(customer)
                        .address(address)
                        .status(OrderStatus.CREATED)
                        .totalAmount(new BigDecimal("50000.00"))
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        orderRepository.save(
                Order.builder()
                        .customer(customer)
                        .address(address)
                        .status(OrderStatus.PAID)
                        .totalAmount(new BigDecimal("90000.00"))
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        List<Order> found = orderRepository.findByCustomerId(customer.getId());

        assertThat(found).hasSize(2);
    }

    @Test
    @DisplayName("Debe buscar pedidos por filtros combinados")
    void shouldFindOrdersByFilters() {
        Customer customer = customerRepository.save(
                Customer.builder()
                        .firstName("Ana")
                        .lastName("Torres")
                        .email("cliente2@test.com")
                        .status(CustomerStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Address address = addressRepository.save(
                Address.builder()
                        .customer(customer)
                        .addressLine("Carrera 5")
                        .city("Barranquilla")
                        .postalCode("080001")
                        .build()
        );

        orderRepository.save(
                Order.builder()
                        .customer(customer)
                        .address(address)
                        .status(OrderStatus.PAID)
                        .totalAmount(new BigDecimal("150000.00"))
                        .createdAt(LocalDateTime.of(2026, 4, 1, 10, 0))
                        .build()
        );

        orderRepository.save(
                Order.builder()
                        .customer(customer)
                        .address(address)
                        .status(OrderStatus.CANCELLED)
                        .totalAmount(new BigDecimal("50000.00"))
                        .createdAt(LocalDateTime.of(2026, 4, 2, 10, 0))
                        .build()
        );

        List<Order> found = orderRepository.findOrdersByFilters(
                customer.getId(),
                OrderStatus.PAID,
                LocalDateTime.of(2026, 4, 1, 0, 0),
                LocalDateTime.of(2026, 4, 30, 23, 59),
                new BigDecimal("100000.00"),
                new BigDecimal("200000.00")
        );

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(found.get(0).getTotalAmount()).isEqualByComparingTo("150000.00");
    }

    @Test
    @DisplayName("Debe obtener clientes con mayor facturación")
    void shouldFindTopCustomersByBilling() {
        Customer customer1 = customerRepository.save(
                Customer.builder()
                        .firstName("Carlos")
                        .lastName("Lopez")
                        .email("carlos@test.com")
                        .status(CustomerStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Customer customer2 = customerRepository.save(
                Customer.builder()
                        .firstName("Maria")
                        .lastName("Diaz")
                        .email("maria@test.com")
                        .status(CustomerStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Address address1 = addressRepository.save(
                Address.builder()
                        .customer(customer1)
                        .addressLine("Calle 10")
                        .city("Santa Marta")
                        .postalCode("470002")
                        .build()
        );

        Address address2 = addressRepository.save(
                Address.builder()
                        .customer(customer2)
                        .addressLine("Calle 20")
                        .city("Santa Marta")
                        .postalCode("470003")
                        .build()
        );

        orderRepository.save(
                Order.builder()
                        .customer(customer1)
                        .address(address1)
                        .status(OrderStatus.PAID)
                        .totalAmount(new BigDecimal("300000.00"))
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        orderRepository.save(
                Order.builder()
                        .customer(customer2)
                        .address(address2)
                        .status(OrderStatus.DELIVERED)
                        .totalAmount(new BigDecimal("100000.00"))
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        List<Object[]> result = orderRepository.findTopCustomersByBilling();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)[0]).isEqualTo(customer1.getId());
        assertThat(result.get(0)[1]).isEqualTo("Carlos");
        assertThat(result.get(0)[2]).isEqualTo("Lopez");
    }

    @Test
    @DisplayName("Debe obtener ingresos mensuales agrupados")
    void shouldFindMonthlyIncome() {
        Customer customer = customerRepository.save(
                Customer.builder()
                        .firstName("Laura")
                        .lastName("Ruiz")
                        .email("laura@test.com")
                        .status(CustomerStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Address address = addressRepository.save(
                Address.builder()
                        .customer(customer)
                        .addressLine("Av universidad")
                        .city("Santa Marta")
                        .postalCode("470004")
                        .build()
        );

        orderRepository.save(
                Order.builder()
                        .customer(customer)
                        .address(address)
                        .status(OrderStatus.PAID)
                        .totalAmount(new BigDecimal("120000.00"))
                        .createdAt(LocalDateTime.of(2026, 3, 10, 12, 0))
                        .build()
        );

        orderRepository.save(
                Order.builder()
                        .customer(customer)
                        .address(address)
                        .status(OrderStatus.SHIPPED)
                        .totalAmount(new BigDecimal("80000.00"))
                        .createdAt(LocalDateTime.of(2026, 3, 15, 12, 0))
                        .build()
        );

        List<Object[]> result = orderRepository.findMonthlyIncome();

        assertThat(result).isNotEmpty();
        assertThat(((Number) result.get(0)[0]).intValue()).isEqualTo(2026);
        assertThat(((Number) result.get(0)[1]).intValue()).isEqualTo(3);
        assertThat((BigDecimal) result.get(0)[2]).isEqualByComparingTo("200000.00");
    }

}
