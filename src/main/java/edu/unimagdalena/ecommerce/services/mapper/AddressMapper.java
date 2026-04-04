package edu.unimagdalena.ecommerce.services.mapper;


import edu.unimagdalena.ecommerce.api.dto.AddressDtos.AddressResponse;
import edu.unimagdalena.ecommerce.api.dto.AddressDtos.CreateAddressRequest;
import edu.unimagdalena.ecommerce.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Address toEntity(CreateAddressRequest req);

    @Mapping(source = "customer.id", target = "customerId")
    AddressResponse toResponse(Address entity);
}