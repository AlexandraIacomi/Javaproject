package ro.foame.employee_backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.foame.employee_backend.entities.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    boolean existsByName(String name);
}