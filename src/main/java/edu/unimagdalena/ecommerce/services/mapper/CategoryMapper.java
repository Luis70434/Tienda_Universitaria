package edu.unimagdalena.ecommerce.services.mapper;

import edu.unimagdalena.ecommerce.api.dto.CategoryDtos.CategoryResponse;
import edu.unimagdalena.ecommerce.api.dto.CategoryDtos.CreateCategoryRequest;
import edu.unimagdalena.ecommerce.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toEntity(CreateCategoryRequest req);

    CategoryResponse toResponse(Category entity);
}