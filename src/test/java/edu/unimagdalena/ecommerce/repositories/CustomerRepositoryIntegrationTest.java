package edu.unimagdalena.ecommerce.repositories;

import edu.unimagdalena.ecommerce.entities.Customer;
import edu.unimagdalena.ecommerce.enums.CustomerStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerRepositoryIntegrationTest extends AbstractRepositoryTI {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Debe guardar y buscar un cliente por id")
    void shouldSaveAndFindCustomerById() {
        Customer customer = customerRepository.save(
                Customer.builder()
                        .firstName("Luis")
                        .lastName("Perez")
                        .email("luis@test.com")
                        .status(CustomerStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Optional<Customer> found = customerRepository.findById(customer.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Luis");
        assertThat(found.get().getEmail()).isEqualTo("luis@test.com");
    }

    @Test
    @DisplayName("No debe permitir emails duplicados")
    void shouldNotAllowDuplicateEmail() {
        customerRepository.saveAndFlush(
                Customer.builder()
                        .firstName("Carlos")
                        .lastName("Gomez")
                        .email("duplicado@test.com")
                        .status(CustomerStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Customer duplicate = Customer.builder()
                .firstName("Ana")
                .lastName("Ruiz")
                .email("duplicado@test.com")
                .status(CustomerStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            customerRepository.saveAndFlush(duplicate);
        });
    }
}
