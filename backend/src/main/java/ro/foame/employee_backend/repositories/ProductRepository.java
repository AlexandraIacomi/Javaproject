package ro.foame.employee_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.foame.employee_backend.entities.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name, String description, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE LOWER(c.name) = LOWER(:name)")
    Page<Product> findByCategoryNameIgnoreCase(@Param("name") String name, Pageable pageable);
}
