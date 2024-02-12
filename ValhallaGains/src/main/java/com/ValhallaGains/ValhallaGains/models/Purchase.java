package com.ValhallaGains.ValhallaGains.models;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime data;
    @Enumerated(EnumType.STRING)
    private Statustype type;
    private Integer totalAmount;
    private String numberOfPurchase;
    @ManyToOne
    private Client client;
    @OneToMany(mappedBy = "purchase", fetch = FetchType.EAGER)
    private List<PurchaseProduct> purchaseProducts;
    public Purchase() {
    }

    public Purchase(LocalDateTime data, Statustype type, Integer totalAmount,String numberOfPurchase) {
        this.data = data;
        this.type = type;
        this.totalAmount= totalAmount;
        this.numberOfPurchase=numberOfPurchase;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Statustype getType() {
        return type;
    }

    public void setType(Statustype type) {
        this.type = type;
    }

    public String getNumberOfPurchase() {
        return numberOfPurchase;
    }

    public void setNumberOfPurchase(String numberOfPurchase) {
        this.numberOfPurchase = numberOfPurchase;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<PurchaseProduct> getPurchaseProducts() {
        return purchaseProducts;
    }

    public void setPurchaseProducts(List<PurchaseProduct> purchaseProducts) {
        this.purchaseProducts = purchaseProducts;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }
}
