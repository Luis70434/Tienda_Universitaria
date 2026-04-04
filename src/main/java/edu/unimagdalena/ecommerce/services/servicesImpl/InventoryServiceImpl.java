package edu.unimagdalena.ecommerce.services.servicesImpl;


import edu.unimagdalena.ecommerce.api.dto.InventoryDtos.InventoryResponse;
import edu.unimagdalena.ecommerce.api.dto.InventoryDtos.UpdateInventoryRequest;
import edu.unimagdalena.ecommerce.entities.Inventory;
import edu.unimagdalena.ecommerce.repositories.InventoryRepository;
import edu.unimagdalena.ecommerce.services.InventoryService;
import edu.unimagdalena.ecommerce.services.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final InventoryMapper mapper;

    @Override
    public InventoryResponse update(UUID id, UpdateInventoryRequest req) {

        if (req.availableQuantity() < 0 || req.minimumStock() < 0) {
            throw new IllegalArgumentException("El inventario disponible y el stock mínimo no pueden ser negativos.");
        }

        Inventory inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario con ID " + id + " no encontrado"));

        inventory.setAvailableQuantity(req.availableQuantity());
        inventory.setMinimumStock(req.minimumStock());

        return mapper.toResponse(inventoryRepo.save(inventory));
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse get(UUID id) {
        return inventoryRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Inventario con ID " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> list() {
        return inventoryRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        inventoryRepo.deleteById(id);
    }
}