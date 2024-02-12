package com.ValhallaGains.ValhallaGains.dtos;

import com.ValhallaGains.ValhallaGains.models.Purchase;
import com.ValhallaGains.ValhallaGains.models.Statustype;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseDTO {
    private Long id;
    private LocalDateTime data;
    private Statustype type;
    private String numberOfPurchase;

    private Integer totalAmount;


    public PurchaseDTO(Purchase purchase){
        id= purchase.getId();
        data= purchase.getData();
        type= purchase.getType();
        totalAmount= purchase.getTotalAmount();
        numberOfPurchase= purchase.getNumberOfPurchase();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getData() {
        return data;
    }


    public Statustype getType() {
        return type;
    }

    public String getNumberOfPurchase() {
        return numberOfPurchase;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }
}