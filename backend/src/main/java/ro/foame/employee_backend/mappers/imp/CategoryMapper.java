package ro.foame.employee_backend.mappers.imp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ro.foame.employee_backend.dtos.CategoryDto;
import ro.foame.employee_backend.entities.Category;
import ro.foame.employee_backend.mappers.Mapper;


@Component
public class CategoryMapper implements Mapper<Category, CategoryDto> {
    private final ModelMapper modelMapper;

    CategoryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto mapTo(Category category) {
        CategoryDto dto = modelMapper.map(category,CategoryDto.class);
        return dto;
    }

    @Override
    public Category mapFrom(CategoryDto categoryDto) {
        Category dto = modelMapper.map(categoryDto, Category.class);
        return dto;
    }
}
