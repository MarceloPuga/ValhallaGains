package com.ValhallaGains.ValhallaGains.services;

import com.ValhallaGains.ValhallaGains.dtos.NewPurchaseDTO;
import com.ValhallaGains.ValhallaGains.dtos.PurchaseDTO;
import com.ValhallaGains.ValhallaGains.models.Client;
import com.ValhallaGains.ValhallaGains.models.Product;
import com.ValhallaGains.ValhallaGains.models.Purchase;
import com.ValhallaGains.ValhallaGains.models.PurchaseProduct;
import com.ValhallaGains.ValhallaGains.repositories.ClientRepository;
import com.ValhallaGains.ValhallaGains.repositories.ProductRepository;
import com.ValhallaGains.ValhallaGains.repositories.PurchaseProductRepository;
import com.ValhallaGains.ValhallaGains.repositories.PurchaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseProductRepository purchaseProductRepository;

    @Transactional
    public Purchase createPurchase(NewPurchaseDTO newPurchaseDTO, String userEmail) {
        Client client = clientRepository.findByEmail(userEmail);
        // Validate that the client exists
        if (client == null) {
            throw new RuntimeException("You are not authenticated. Log in or register.");
        }

        // Validate the size of the lists (quantities and products)
        if (newPurchaseDTO.quantities().size() != newPurchaseDTO.nombreProductos().size()) {
            throw new RuntimeException("The lists (quantities and products) must be of the same size.");
        }

        // Create the purchase entity
        Purchase purchase = new Purchase();
        purchase.setClient(client);
        purchase.setTotalAmount(0);
        purchase.setData(LocalDateTime.now());
        String numberOfPurchase;
        do {
            numberOfPurchase = String.valueOf(getRandomNumber(00000000, 99999999));
        } while (purchaseRepository.existsByNumberOfPurchase(numberOfPurchase));
        purchase.setNumberOfPurchase(numberOfPurchase);

        List<PurchaseProduct> purchaseProducts = new ArrayList<>();

        for (int i = 0; i < newPurchaseDTO.quantities().size(); i++) {
            Integer quantity = newPurchaseDTO.quantities().get(i);
            String productName = newPurchaseDTO.nombreProductos().get(i);
            Product product = productRepository.findByName(productName);

            // Validate that the product exists
            if (product == null) {
                throw new RuntimeException("PRODUCT DOES NOT EXIST: " + productName);
            }

            Long stock = product.getStock();
            if (quantity > stock) {
                throw new RuntimeException("The requested quantity exceeds the available stock of the product: " + product.getName());
            }

            product.setStock(stock - quantity);
            productRepository.save(product);
            Integer finalProductPrice = (int) (quantity * product.getPrice());

            purchase.setTotalAmount(purchase.getTotalAmount() + finalProductPrice);

            PurchaseProduct purchaseProduct = new PurchaseProduct(quantity);
            purchaseProduct.setProduct(product);
            purchaseProduct.setPurchase(purchase);
            purchaseProducts.add(purchaseProduct);
        }

        purchase.setPurchaseProducts(purchaseProducts);

        purchaseRepository.save(purchase);
        purchaseProductRepository.saveAll(purchaseProducts);

        return purchase;
    }
    public List<PurchaseDTO> getAllPurchases() {
        return purchaseRepository.findAll()
                .stream()
                .map(PurchaseDTO::new)
                .collect(Collectors.toList());
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
