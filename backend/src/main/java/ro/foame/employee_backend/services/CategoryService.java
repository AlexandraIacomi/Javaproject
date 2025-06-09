package ro.foame.employee_backend.services;

import org.springframework.stereotype.Service;
import ro.foame.employee_backend.dtos.CategoryDto;
import ro.foame.employee_backend.entities.Category;

import java.util.List;

@Service
public interface CategoryService {
    public List<Category> getCategories();

    public Category getCategoryById(int id);

    public Category createCategory(Category category);

    public Category updateCategory(Integer id, CategoryDto category);

    public void deleteCategory(int id);
}
