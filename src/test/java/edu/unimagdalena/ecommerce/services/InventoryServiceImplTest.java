package edu.unimagdalena.ecommerce.services;

import edu.unimagdalena.ecommerce.api.dto.InventoryDtos.InventoryResponse;
import edu.unimagdalena.ecommerce.api.dto.InventoryDtos.UpdateInventoryRequest;
import edu.unimagdalena.ecommerce.entities.Inventory;
import edu.unimagdalena.ecommerce.repositories.InventoryRepository;
import edu.unimagdalena.ecommerce.services.mapper.InventoryMapper;
import edu.unimagdalena.ecommerce.services.servicesImpl.InventoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository repositorio;

    @Mock
    private InventoryMapper mapeador;

    @InjectMocks
    private InventoryServiceImpl servicio;

    @Test
    @DisplayName("Al actualizar un inventario, si los datos son válidos, debe retornar la respuesta")
    void actualizarInventario_DebeRetornarRespuesta() {
        // Arrange
        UUID id = UUID.randomUUID();
        UpdateInventoryRequest peticion = new UpdateInventoryRequest(100, 10);
        Inventory entidad = new Inventory();
        InventoryResponse respuesta = new InventoryResponse(id, UUID.randomUUID(), 100, 10);

        when(repositorio.findById(id)).thenReturn(Optional.of(entidad));
        when(repositorio.save(any())).thenReturn(entidad);
        when(mapeador.toResponse(entidad)).thenReturn(respuesta);

        // Act
        InventoryResponse resultado = servicio.update(id, peticion);

        // Assert
        assertNotNull(resultado);
        assertEquals(100, resultado.availableQuantity());
        verify(repositorio).save(entidad);
    }

    @Test
    @DisplayName("Al buscar por ID, si existe, debe retornar el inventario")
    void obtenerInventario_DebeRetornarRespuesta() {
        UUID id = UUID.randomUUID();
        Inventory entidad = new Inventory();
        InventoryResponse respuesta = new InventoryResponse(id, UUID.randomUUID(), 50, 5);

        when(repositorio.findById(id)).thenReturn(Optional.of(entidad));
        when(mapeador.toResponse(entidad)).thenReturn(respuesta);

        InventoryResponse resultado = servicio.get(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.id());
    }
}