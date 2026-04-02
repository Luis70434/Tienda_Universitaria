package edu.unimagdalena.ecommerce.repositories;

import edu.unimagdalena.ecommerce.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}