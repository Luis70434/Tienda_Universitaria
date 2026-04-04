package edu.unimagdalena.ecommerce.services.mapper;


import edu.unimagdalena.ecommerce.api.dto.OrderDtos.OrderItemResponse;
import edu.unimagdalena.ecommerce.api.dto.OrderDtos.OrderResponse;
import edu.unimagdalena.ecommerce.entities.Order;
import edu.unimagdalena.ecommerce.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "address.id", target = "addressId")
    OrderResponse toResponse(Order entity);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemResponse toItemResponse(OrderItem entity);
}