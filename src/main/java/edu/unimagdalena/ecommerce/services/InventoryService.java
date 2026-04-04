package edu.unimagdalena.ecommerce.services;


import edu.unimagdalena.ecommerce.api.dto.InventoryDtos.InventoryResponse;
import edu.unimagdalena.ecommerce.api.dto.InventoryDtos.UpdateInventoryRequest;

import java.util.List;
import java.util.UUID;

public interface InventoryService {

    InventoryResponse update(UUID id, UpdateInventoryRequest req);
    InventoryResponse get(UUID id);
    List<InventoryResponse> list();
    void delete(UUID id);
}