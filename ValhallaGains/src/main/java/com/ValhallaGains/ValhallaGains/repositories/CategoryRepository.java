package com.ValhallaGains.ValhallaGains.repositories;

import com.ValhallaGains.ValhallaGains.models.Category;
import com.ValhallaGains.ValhallaGains.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
    boolean existsByName(String categoryName);
    void deleteByName (String name);
}
