package ro.foame.employee_backend.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ro.foame.employee_backend.dtos.CategoryDto;
import ro.foame.employee_backend.entities.Category;
import ro.foame.employee_backend.repositories.CategoryRepository;
import ro.foame.employee_backend.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public List<Category> getCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category createCategory(Category category) {
       return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Integer id, CategoryDto categoryDto) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryDto.getName());
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }
}
