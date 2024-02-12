package com.ValhallaGains.ValhallaGains.models;

import jakarta.persistence.*;

@Entity
public class PurchaseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Purchase purchase;
    @ManyToOne
    private Product product;

    private Integer quantities;

    public PurchaseProduct() {
    }
    public PurchaseProduct(Integer quantities){
        this.quantities = quantities;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantities() {
        return quantities;
    }

    public void setQuantities(Integer quantities) {
        this.quantities = quantities;
    }

}
