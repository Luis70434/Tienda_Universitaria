package edu.unimagdalena.ecommerce.services.mapper;


import edu.unimagdalena.ecommerce.api.dto.CustomerDtos.CreateCustomerRequest;
import edu.unimagdalena.ecommerce.api.dto.CustomerDtos.CustomerResponse;
import edu.unimagdalena.ecommerce.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {


    Customer toEntity(CreateCustomerRequest req);

    CustomerResponse toResponse(Customer entity);
}