package edu.unimagdalena.ecommerce.services;



import edu.unimagdalena.ecommerce.api.dto.ProductDtos.CreateProductRequest;
import edu.unimagdalena.ecommerce.api.dto.ProductDtos.ProductResponse;
import edu.unimagdalena.ecommerce.entities.Category;
import edu.unimagdalena.ecommerce.entities.Inventory;
import edu.unimagdalena.ecommerce.entities.Product;
import edu.unimagdalena.ecommerce.repositories.CategoryRepository;
import edu.unimagdalena.ecommerce.repositories.ProductRepository;
import edu.unimagdalena.ecommerce.services.mapper.ProductMapper;
import edu.unimagdalena.ecommerce.services.servicesImpl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepo;
    @Mock
    private CategoryRepository categoryRepo;
    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductServiceImpl service;


    @Test
    @DisplayName("Regla 6.1: Debe fallar si el SKU ya existe")
    void crearProducto_SkuDuplicado_LanzaExcepcion() {
        CreateProductRequest req = new CreateProductRequest("SKU-DUP", "Nombre", "Desc", new BigDecimal("10"), UUID.randomUUID(), 10, 2);

        when(productRepo.existsBySku("SKU-DUP")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.create(req));
    }
    @Test
    @DisplayName("Regla 6.6: Debe filtrar productos con bajo stock")
    void consultarBajoStock_FiltraCorrectamente() {
        // Producto 1: Bajo stock (5 <= 10)
        Product p1 = createMockProduct(5, 10);
        // Producto 2: Stock normal (20 > 10)
        Product p2 = createMockProduct(20, 10);

        when(productRepo.findAll()).thenReturn(List.of(p1, p2));
        when(mapper.toResponse(p1)).thenReturn(new ProductResponse(UUID.randomUUID(), "S1", "P1", "D1", BigDecimal.TEN, true, UUID.randomUUID(), "C1", 5));

        List<ProductResponse> resultado = service.getLowStock();

        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).stock());
    }

    // Método helper para crear productos de prueba rápidamente
    private Product createMockProduct(int available, int min) {
        Product p = new Product();
        Inventory i = new Inventory();
        i.setAvailableQuantity(available);
        i.setMinimumStock(min);
        p.setInventory(i);
        return p;
    }

    @Test
    @DisplayName(" Debe fallar si el precio es 0 o menor")
    void crearProducto_PrecioInvalido_LanzaExcepcion() {
        CreateProductRequest req = new CreateProductRequest("SKU", "Nombre", "Desc", BigDecimal.ZERO, UUID.randomUUID(), 10, 2);

        assertThrows(IllegalArgumentException.class, () -> service.create(req));
    }

    @Test
    @DisplayName("Debe fallar si el stock es negativo")
    void crearProducto_StockNegativo_LanzaExcepcion() {
        CreateProductRequest req = new CreateProductRequest("SKU", "Nombre", "Desc", new BigDecimal("10"), UUID.randomUUID(), -1, 2);

        assertThrows(IllegalArgumentException.class, () -> service.create(req));
    }

    @Test
    @DisplayName("Debe fallar si la categoría no existe")
    void crearProducto_CategoriaNoExiste_LanzaExcepcion() {
        UUID catId = UUID.randomUUID();
        CreateProductRequest req = new CreateProductRequest("SKU", "Nombre", "Desc", new BigDecimal("10"), catId, 10, 2);

        when(categoryRepo.findById(catId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.create(req));
    }

    @Test
    @DisplayName("Éxito: Al crear producto válido, se debe retornar la respuesta con stock")
    void crearProducto_Exitoso() {
        UUID catId = UUID.randomUUID();
        CreateProductRequest req = new CreateProductRequest("PROD1", "Laptop", "Gamer", new BigDecimal("1500"), catId, 10, 2);

        Category cat = new Category();
        Product product = new Product();
        ProductResponse res = new ProductResponse(UUID.randomUUID(), "PROD1", "Laptop", "Gamer", new BigDecimal("1500"), true, catId, "Electrónica", 10);

        when(categoryRepo.findById(catId)).thenReturn(Optional.of(cat));
        when(mapper.toEntity(req)).thenReturn(product);
        when(productRepo.save(any())).thenReturn(product);
        when(mapper.toResponse(any())).thenReturn(res);

        ProductResponse resultado = service.create(req);

        assertNotNull(resultado);
        assertEquals(10, resultado.stock());
        verify(productRepo).save(any());
    }
}