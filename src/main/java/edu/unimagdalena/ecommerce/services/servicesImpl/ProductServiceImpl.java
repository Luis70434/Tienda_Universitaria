package edu.unimagdalena.ecommerce.services.servicesImpl;




import edu.unimagdalena.ecommerce.api.dto.ProductDtos;
import edu.unimagdalena.ecommerce.api.dto.ProductDtos.CreateProductRequest;
import edu.unimagdalena.ecommerce.api.dto.ProductDtos.ProductResponse;
import edu.unimagdalena.ecommerce.entities.Category;
import edu.unimagdalena.ecommerce.entities.Inventory;
import edu.unimagdalena.ecommerce.entities.Product;
import edu.unimagdalena.ecommerce.exceptions.ResourceNotFoundException;
import edu.unimagdalena.ecommerce.repositories.CategoryRepository;
import edu.unimagdalena.ecommerce.repositories.ProductRepository;
import edu.unimagdalena.ecommerce.services.ProductService;
import edu.unimagdalena.ecommerce.services.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final ProductMapper mapper;

    @Override
    public ProductResponse create(CreateProductRequest req) {
        // Regla 6.1: Precio mayor a cero
        if (req.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor que cero.");
        }

        // Regla 6.1: Inventario y stock mínimo no negativos
        if (req.stock() < 0 || req.minStock() < 0) {
            throw new IllegalArgumentException("El inventario disponible y el stock mínimo no pueden ser negativos.");
        }

        // Regla 6.1: SKU único
        if (productRepo.existsBySku(req.sku())) {
            throw new IllegalArgumentException("Ya existe un producto con el SKU: " + req.sku());
        }

        // Regla 6.1: Categoría existente
        Category category = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("La categoría indicada no existe."));

        Product product = mapper.toEntity(req);
        product.setCategory(category);

        // Configuración del Inventario (Relación 1:1)
        Inventory inventory = new Inventory();
        inventory.setAvailableQuantity(req.stock());
        inventory.setMinimumStock(req.minStock());
        inventory.setProduct(product);

        product.setInventory(inventory);

        Product savedProduct = productRepo.save(product);
        return mapper.toResponse(savedProduct);
    }

    //  CONSULTAS Y REPORTES ---
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStock() {
        return productRepo.findAll().stream()
                .filter(p -> p.getInventory().getAvailableQuantity() <= p.getInventory().getMinimumStock())
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getByCategory(UUID categoryId) {
        return productRepo.findAll().stream()
                .filter(p -> p.getCategory().getId().equals(categoryId))
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(UUID id) {
        return productRepo.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Producto con ID " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepo.findAll().stream().map(mapper::toResponse).toList();
    }


    @Override
    public ProductResponse update(UUID id, ProductDtos.UpdateProductRequest req) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        Category category = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + req.categoryId()));

        product.setName(req.name());
        product.setDescription(req.description());
        product.setPrice(req.price());
        product.setActive(req.active());
        product.setCategory(category);

        Product saved = productRepo.save(product);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        productRepo.deleteById(id);
    }
}