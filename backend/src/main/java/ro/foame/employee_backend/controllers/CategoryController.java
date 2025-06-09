package ro.foame.employee_backend.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.foame.employee_backend.dtos.CategoryDto;
import ro.foame.employee_backend.entities.Category;
import ro.foame.employee_backend.mappers.Mapper;
import ro.foame.employee_backend.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    private final Mapper<Category, CategoryDto> mapper;

    public CategoryController(CategoryService categoryService, Mapper<Category, CategoryDto> mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @GetMapping("/all")
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryService.getCategories();
        return categories.stream().map(mapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id);
        return (category != null)
                ? ResponseEntity.ok(mapper.mapTo(category))
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = mapper.mapFrom(categoryDto);
        Category created = categoryService.createCategory(category);
        return new ResponseEntity<>(mapper.mapTo(created), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto) {
        try {
            Category category = categoryService.updateCategory(id, categoryDto);
            return ResponseEntity.ok(mapper.mapTo(category));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
