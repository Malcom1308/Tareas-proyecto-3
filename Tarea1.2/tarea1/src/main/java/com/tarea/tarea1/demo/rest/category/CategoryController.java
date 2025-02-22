package com.tarea.tarea1.demo.rest.category;


import com.tarea.tarea1.demo.logic.entity.category.Category;
import com.tarea.tarea1.demo.logic.entity.category.CategoryRepository;
import com.tarea.tarea1.demo.logic.entity.http.GlobalResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId,@RequestBody Category category, HttpServletRequest request) {
        Optional<Category> foundOrder = categoryRepository.findById(categoryId);
        if(foundOrder.isPresent()) {
            category.setId(foundOrder.get().getId());
            categoryRepository.save(category);
            return new GlobalResponseHandler().handleResponse(
                    "Category update successfully",
                            category,
                            HttpStatus.OK,
                            request);
        } else{
            return new GlobalResponseHandler().handleResponse(
                    "Category id " + categoryId + " not found",
                    HttpStatus.NOT_FOUND,
                    request);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, HttpServletRequest request) {
        Optional<Category> foundCategory = categoryRepository.findById(id);
        if(foundCategory.isPresent()) {
            categoryRepository.deleteById(id);
            return new GlobalResponseHandler().handleResponse(
                    "Category successfuly deleted",
                    HttpStatus.OK,
                    request);
        } else{
            return new GlobalResponseHandler().handleResponse(
                    "Category with id " + id + " not found ",
                    HttpStatus.NOT_FOUND,
                    request);
        }
    }

}
