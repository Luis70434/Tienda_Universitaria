package edu.unimagdalena.ecommerce.services.servicesImpl;


import edu.unimagdalena.ecommerce.api.dto.CustomerDtos;
import edu.unimagdalena.ecommerce.api.dto.CustomerDtos.CreateCustomerRequest;
import edu.unimagdalena.ecommerce.api.dto.CustomerDtos.CustomerResponse;
import edu.unimagdalena.ecommerce.entities.Customer;
import edu.unimagdalena.ecommerce.exceptions.ResourceNotFoundException;
import edu.unimagdalena.ecommerce.repositories.CustomerRepository;
import edu.unimagdalena.ecommerce.services.CustomerService;
import edu.unimagdalena.ecommerce.services.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;
    private final CustomerMapper mapper;

    @Override
    public CustomerResponse create(CreateCustomerRequest req) {
        var customerEntity = mapper.toEntity(req);
        var entitySaved = repo.save(customerEntity);
        return mapper.toResponse(entitySaved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse get(UUID id) {
        return repo.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> list() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public CustomerDtos.CustomerResponse update(UUID id, CustomerDtos.UpdateCustomerRequest req) {
        Customer customer = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        customer.setFirstName(req.firstName());
        customer.setLastName(req.lastName());
        customer.setEmail(req.email());
        customer.setStatus(req.status());

        Customer saved = repo.save(customer);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        repo.deleteById(id);
    }
}