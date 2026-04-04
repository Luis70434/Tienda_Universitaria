package edu.unimagdalena.ecommerce.services.mapper;

import edu.unimagdalena.ecommerce.api.dto.ProductDtos.CreateProductRequest;
import edu.unimagdalena.ecommerce.api.dto.ProductDtos.ProductResponse;
import edu.unimagdalena.ecommerce.api.dto.ProductDtos;
import edu.unimagdalena.ecommerce.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "inventory", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Product toEntity(CreateProductRequest req);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "inventory.availableQuantity", target = "stock")
    ProductResponse toResponse(Product entity);
}
