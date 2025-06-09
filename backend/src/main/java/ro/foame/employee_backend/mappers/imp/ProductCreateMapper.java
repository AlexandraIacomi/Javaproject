package ro.foame.employee_backend.mappers.imp;

import org.springframework.stereotype.Component;
import ro.foame.employee_backend.dtos.ProductCreateDto;
import ro.foame.employee_backend.entities.Category;
import ro.foame.employee_backend.entities.Product;
import ro.foame.employee_backend.mappers.Mapper;
import ro.foame.employee_backend.repositories.CategoryRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductCreateMapper implements Mapper<Product, ProductCreateDto> {

    private final CategoryRepository categoryRepository;

    public ProductCreateMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductCreateDto mapTo(Product product) {
        return ProductCreateDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .slug(product.getSlug())
                .categoriesIds(product.getCategories().stream().map(Category::getId).collect(Collectors.toList()))
                .build();
    }

    @Override
    public Product mapFrom(ProductCreateDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());

        if (dto.getCategoriesIds() != null) {
            Set<Category> categories = dto.getCategoriesIds().stream()
                    .map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id)))
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }

        return product;
    }

    @Override
    public void mapFromDtoToEntity(ProductCreateDto dto, Product product) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());

        if (dto.getCategoriesIds() != null) {
            Set<Category> categories = dto.getCategoriesIds().stream()
                    .map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id)))
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }
    }
}