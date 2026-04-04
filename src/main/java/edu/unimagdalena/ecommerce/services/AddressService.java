package edu.unimagdalena.ecommerce.services;



import edu.unimagdalena.ecommerce.api.dto.AddressDtos.AddressResponse;
import edu.unimagdalena.ecommerce.api.dto.AddressDtos.CreateAddressRequest;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressResponse create(CreateAddressRequest req);
    AddressResponse get(UUID id);
    List<AddressResponse> listByCustomer(UUID customerId);
    void delete(UUID id);
}