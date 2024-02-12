package com.ValhallaGains.ValhallaGains.dtos;

import com.ValhallaGains.ValhallaGains.models.PurchaseProduct;

public class PurchaseProductDTO {
    private Integer quantities;

    public PurchaseProductDTO(PurchaseProduct purchaseProduct) {
        quantities = purchaseProduct.getQuantities();
    }

    public Integer getQuantities() {
        return quantities;
    }
}
