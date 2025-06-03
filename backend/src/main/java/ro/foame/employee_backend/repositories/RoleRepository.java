package ro.foame.employee_backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ro.foame.employee_backend.entities.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}