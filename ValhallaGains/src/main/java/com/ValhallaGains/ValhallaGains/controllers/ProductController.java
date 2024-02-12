package com.ValhallaGains.ValhallaGains.controllers;

import com.ValhallaGains.ValhallaGains.dtos.ProductDTO;
import com.ValhallaGains.ValhallaGains.models.Category;
import com.ValhallaGains.ValhallaGains.models.Product;
import com.ValhallaGains.ValhallaGains.repositories.CategoryRepository;
import com.ValhallaGains.ValhallaGains.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("/product")
    public ResponseEntity<Object> createProduct(@RequestParam String name, @RequestParam String description,
                                                @RequestParam double price, @RequestParam long stock,
                                                @RequestParam String nameCategory, @RequestParam String imageURL){
        //CREO PRODUCTO
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setPrice(price);
        newProduct.setStock(stock);
        newProduct.setImageURL(imageURL);
        if (categoryRepository.findByName(nameCategory) == null) {
            return new ResponseEntity<>("Category not found for the product.", HttpStatus.BAD_REQUEST);
        }
        Category newCategory= categoryRepository.findByName(nameCategory);
        newProduct.setCategory(newCategory);
        //GUARDO PRODUCTO
        productRepository.save(newProduct);


        return new ResponseEntity<>("Product created successfully", HttpStatus.CREATED);
    }
    @GetMapping("/product")
    public List<ProductDTO>getProducts(){
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductDTO(product)).collect(Collectors.toList());
    }
    @GetMapping("/product/count")
    public ResponseEntity<Long> getProductCount() {
        Long productCount = productRepository.count();
        return new ResponseEntity<>(productCount, HttpStatus.OK);
    }
    @DeleteMapping("/product/delete")
    public ResponseEntity<Object> deleteProduct(@RequestParam String nombrProducto) {
        Product productToDelete = productRepository.findByName(nombrProducto);
        if (productToDelete == null) {
            return new ResponseEntity<>("The product does not exist.", HttpStatus.BAD_REQUEST);
        }

        productRepository.delete(productToDelete);

        return new ResponseEntity<>("Good",HttpStatus.OK);
    }


}
