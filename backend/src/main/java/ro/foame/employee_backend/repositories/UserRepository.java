package ro.foame.employee_backend.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.foame.employee_backend.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
