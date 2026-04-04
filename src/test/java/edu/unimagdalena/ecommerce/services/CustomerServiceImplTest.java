package edu.unimagdalena.ecommerce.services;

import edu.unimagdalena.ecommerce.api.dto.CustomerDtos.CreateCustomerRequest;
import edu.unimagdalena.ecommerce.api.dto.CustomerDtos.CustomerResponse;
import edu.unimagdalena.ecommerce.entities.Customer;
import edu.unimagdalena.ecommerce.enums.CustomerStatus;
import edu.unimagdalena.ecommerce.repositories.CustomerRepository;
import edu.unimagdalena.ecommerce.services.mapper.CustomerMapper;
import edu.unimagdalena.ecommerce.services.servicesImpl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repo;

    @Mock
    private CustomerMapper mapper;

    @InjectMocks
    private CustomerServiceImpl service;

    @Test
    void crearCliente_DebeRetornarRespuestaExitosa() {
        // Arrange
        CreateCustomerRequest request = new CreateCustomerRequest("Juan", "Perez", "juan@test.com");
        Customer entity = new Customer();
        entity.setFirstName("Juan");
        CustomerResponse response = new CustomerResponse(UUID.randomUUID(), "Juan", "Perez", "juan@test.com", CustomerStatus.ACTIVE, LocalDateTime.now());
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repo.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        // Act
        CustomerResponse result = service.create(request);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.firstName());
        verify(repo, times(1)).save(entity);
    }

    @Test
    void obtenerCliente_CuandoExiste_DebeRetornarRespuesta() {
        // Arrange
        UUID id = UUID.randomUUID();
        Customer entity = new Customer();
        CustomerResponse response = new CustomerResponse(id, "Ana", "Gomez", "ana@test.com", CustomerStatus.ACTIVE, LocalDateTime.now());
        when(repo.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        // Act
        CustomerResponse result = service.get(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.id());
    }
}