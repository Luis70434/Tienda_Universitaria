package edu.unimagdalena.ecommerce.services.servicesImpl;


import edu.unimagdalena.ecommerce.api.dto.AddressDtos.AddressResponse;
import edu.unimagdalena.ecommerce.api.dto.AddressDtos.CreateAddressRequest;
import edu.unimagdalena.ecommerce.entities.Address;
import edu.unimagdalena.ecommerce.entities.Customer;
import edu.unimagdalena.ecommerce.repositories.AddressRepository;
import edu.unimagdalena.ecommerce.repositories.CustomerRepository;
import edu.unimagdalena.ecommerce.services.AddressService;
import edu.unimagdalena.ecommerce.services.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepo;
    private final CustomerRepository customerRepo;
    private final AddressMapper mapper;

    @Override
    public AddressResponse create(CreateAddressRequest req) {
        // Validamos que el cliente exista antes de asignarle la dirección
        Customer customer = customerRepo.findById(req.customerId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Address address = mapper.toEntity(req);
        address.setCustomer(customer);

        return mapper.toResponse(addressRepo.save(address));
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse get(UUID id) {
        return addressRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> listByCustomer(UUID customerId) {
        // Asumiendo que tienes findByCustomerId en el repositorio
        return addressRepo.findByCustomerId(customerId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        addressRepo.deleteById(id);
    }
}