package com.ValhallaGains.ValhallaGains.controllers;
import com.ValhallaGains.ValhallaGains.dtos.CategoryDTO;
import com.ValhallaGains.ValhallaGains.models.Category;
import com.ValhallaGains.ValhallaGains.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CategoryController {


    @Autowired
    CategoryRepository categoryRepository;
    @GetMapping("/category")
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());

    }
    @GetMapping("/category/count")
    public ResponseEntity<Long> getCategoryCount() {
        long count = categoryRepository.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryByName(@PathVariable Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> new ResponseEntity<>(new CategoryDTO(category), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/category")
    public ResponseEntity<Object> createCategory(
            @RequestParam String name) {
        if (categoryRepository.findByName(name) != null){
            return new ResponseEntity<>("the category "+ name+" already exists.", HttpStatus.BAD_REQUEST);
        }
        Category category = new Category(name);
        categoryRepository.save(category);
        return new ResponseEntity<>("Category "+ name+" created successful",HttpStatus.CREATED);
    }

    @DeleteMapping("/category")
    public ResponseEntity<Object> deleteCategory(@RequestParam String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            categoryRepository.deleteByName(categoryName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
