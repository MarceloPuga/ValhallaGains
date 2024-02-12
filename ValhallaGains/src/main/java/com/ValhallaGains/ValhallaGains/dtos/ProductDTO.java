package com.ValhallaGains.ValhallaGains.dtos;

import com.ValhallaGains.ValhallaGains.models.Category;
import com.ValhallaGains.ValhallaGains.models.Product;
import com.ValhallaGains.ValhallaGains.models.Purchase;

import java.util.List;

public class ProductDTO {

    private Long id;

    private String name, description;

    private double price;

    private long stock;

/*
    private Purchase purchase;
*/
    private String imageURL;

    private Category category;

    public ProductDTO(Product product) {
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        stock = product.getStock();
        imageURL = product.getImageURL();
        category = product.getCategory();

    }

    public String getImageURL() {
        return imageURL;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public long getStock() {
        return stock;
    }

    public Category getCategory() {
        return category;
    }
}