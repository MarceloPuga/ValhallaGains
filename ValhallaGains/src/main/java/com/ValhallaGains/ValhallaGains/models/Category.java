package com.ValhallaGains.ValhallaGains.models;
import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    // Constructores, getters y setters

    // Constructor sin argumentos para JPA
    public Category() {}

    public Category(String name) {

        this.name = name;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getProductIds() {
        return products.stream()
                .map(Product::getId)
                .collect( Collectors.toList());
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}