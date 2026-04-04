package edu.unimagdalena.ecommerce.services.mapper;



import edu.unimagdalena.ecommerce.api.dto.InventoryDtos.InventoryResponse;
import edu.unimagdalena.ecommerce.entities.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InventoryMapper {

    @Mapping(source = "product.id", target = "productId")
    InventoryResponse toResponse(Inventory entity);
}