package edu.unimagdalena.ecommerce.security.repo;

import edu.unimagdalena.ecommerce.security.domine.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByUsername(String username);
}
