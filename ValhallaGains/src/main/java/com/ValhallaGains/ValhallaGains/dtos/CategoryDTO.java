package com.ValhallaGains.ValhallaGains.dtos;
import com.ValhallaGains.ValhallaGains.models.Category;

import java.util.List;

public class CategoryDTO {

    private Long id;
    private String name;
    private List<Long> products;

    public CategoryDTO(Category category) {
        id = category.getId();
        name = category.getName();
        products = category.getProductIds();
    }

    // Métodos getter

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Long> getProductIds() {
        return products;
    }
}