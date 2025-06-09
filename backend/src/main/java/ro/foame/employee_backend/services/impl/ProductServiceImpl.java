package ro.foame.employee_backend.services.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.foame.employee_backend.dtos.ProductCreateDto;
import ro.foame.employee_backend.dtos.ProductDto;
import ro.foame.employee_backend.entities.Category;
import ro.foame.employee_backend.entities.Product;
import ro.foame.employee_backend.mappers.Mapper;
import ro.foame.employee_backend.repositories.CategoryRepository;
import ro.foame.employee_backend.repositories.ProductRepository;
import ro.foame.employee_backend.services.ProductService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Mapper<Product, ProductDto> mapper;
    private final Mapper<Product, ProductCreateDto> createmapper;
    private final CategoryRepository categoryRepository;


    public ProductServiceImpl(ProductRepository productRepository, Mapper<Product, ProductDto> mapper, Mapper<Product, ProductCreateDto> createmapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.createmapper = createmapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product save(Product product) {
       return productRepository.save(product);
    }

    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(mapper::mapTo);
    }

    @Override
    public Page<ProductDto> searchProducts(String searchTerm, Pageable pageable) {
        Page<Product> productPage = productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm, searchTerm, pageable);
        return productPage.map(mapper::mapTo);
    }

    @Override
    public Product getProductById(Integer id) {
        return null;
    }


    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<ProductDto> getProductBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .map(mapper::mapTo);
    }

    @Override
    public boolean isExistsBySlug(String slug) {
        return productRepository.existsBySlug(slug);
    }

    @Override
    public ProductCreateDto partialUpdate(String slug, ProductCreateDto productDto) {
        Product existingProduct = productRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + slug));

        if (productDto.getName() != null) {
            existingProduct.setName(productDto.getName());
        }
        if (productDto.getImageUrl() != null) {
            existingProduct.setImageUrl(productDto.getImageUrl());
        }
        if (productDto.getPrice() != null) {
            existingProduct.setPrice(productDto.getPrice());
        }
        if (productDto.getDescription() != null) {
            existingProduct.setDescription(productDto.getDescription());
        }
        if (productDto.getCategoriesIds() != null) {
            Set<Category> categories = productDto.getCategoriesIds().stream()
                    .map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id)))
                    .collect(Collectors.toSet());
            existingProduct.setCategories(categories);
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return createmapper.mapTo(updatedProduct);
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public ProductDto createProduct(ProductCreateDto productDto) {
        Product product = createmapper.mapFrom(productDto);
        if(productRepository.existsBySlug(product.getSlug())) {
            throw new EntityExistsException("Slug already exists: " + product.getSlug());
        }
        productRepository.save(product);
        return mapper.mapTo(product);
    }

}
