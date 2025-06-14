package ro.foame.employee_backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.foame.employee_backend.dtos.ProductCreateDto;
import ro.foame.employee_backend.dtos.ProductDto;
import ro.foame.employee_backend.entities.Product;

import java.util.Optional;

@Service
public interface ProductService {
    Product save(Product product);

    Page<ProductDto> getAllProducts(Pageable pageable);

    Product getProductById(Integer id);

    void deleteProduct(Integer id);

    Optional<ProductDto> getProductBySlug (String slug);

    boolean isExistsBySlug(String slug);

    ProductCreateDto partialUpdate(String slug, ProductCreateDto existingProduct);

    boolean existsByName(String name);

    ProductDto createProduct(ProductCreateDto productDto);

    Page<ProductDto> searchProducts(String searchTerm, Pageable pageable);
    Page<ProductDto> filterByCategory(String categoryName, Pageable pageable);
}
