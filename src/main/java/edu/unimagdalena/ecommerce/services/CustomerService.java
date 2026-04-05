package edu.unimagdalena.ecommerce.services;


import edu.unimagdalena.ecommerce.api.dto.CustomerDtos;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerDtos.CustomerResponse create(CustomerDtos.CreateCustomerRequest req);
    CustomerDtos.CustomerResponse get(UUID id);
    List<CustomerDtos.CustomerResponse> list();
    CustomerDtos.CustomerResponse update(UUID id, CustomerDtos.UpdateCustomerRequest req);
    void delete(UUID id);
}