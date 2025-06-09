package ro.foame.employee_backend.mappers.imp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ro.foame.employee_backend.dtos.CategoryDto;
import ro.foame.employee_backend.dtos.ProductDto;
import ro.foame.employee_backend.entities.Category;
import ro.foame.employee_backend.entities.Product;
import ro.foame.employee_backend.mappers.Mapper;
import ro.foame.employee_backend.repositories.CategoryRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductMapper implements Mapper<Product, ProductDto> {

    private ModelMapper modelMapper;
    private CategoryRepository categoryRepository;

    ProductMapper(ModelMapper modelMapper , CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDto mapTo(Product product) {
        ProductDto dto = modelMapper.map(product, ProductDto.class);
        if (product.getCategories() != null) {
            Set<CategoryDto> categoryDtos = product.getCategories().stream()
                    .map(cat -> CategoryDto.builder()
                            .id(cat.getId())
                            .name(cat.getName())
                            .build())
                    .collect(Collectors.toSet());
            dto.setCategories(categoryDtos);
        }
        return dto;
    }

    @Override
    public Product mapFrom(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        if (productDto.getCategories() != null) {
            Set<Category> categories = productDto.getCategories().stream()
                    .map(catDto -> categoryRepository.findById(catDto.getId())
                            .orElseThrow(() -> new RuntimeException("Category not found with id: " + catDto.getId())))
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }
        return product;
    }
    @Override
    public void mapFromDtoToEntity(ProductDto productDto, Product product) {
        modelMapper.map(productDto, product);

        if (productDto.getCategories() != null) {
            Set<Category> categories = productDto.getCategories().stream()
                    .map(catDto -> categoryRepository.findById(catDto.getId())
                            .orElseThrow(() -> new RuntimeException("Category not found with id: " + catDto.getId())))
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }
    }
}

