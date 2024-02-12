package com.ValhallaGains.ValhallaGains.dtos;

import com.ValhallaGains.ValhallaGains.models.Client;
import com.ValhallaGains.ValhallaGains.models.Direction;
import com.ValhallaGains.ValhallaGains.models.Purchase;
import com.ValhallaGains.ValhallaGains.models.Trainingtype;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private Long id;
    private Trainingtype type=Trainingtype.NONE;
    private String name;
    private String email;
    private List<PurchaseDTO> purchase;
    private String lastName;

    public ClientDTO(Client client) {
        id = client.getId();
        name = client.getName();
        email = client.getEmail();
        lastName = client.getLastName();
        type=client.getType();
        purchase = client.getPurchases()
                .stream()
                .map(purchase -> new PurchaseDTO(purchase)).collect(Collectors.toList());

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public List<PurchaseDTO> getPurchase() {
        return purchase;
    }

    public Trainingtype getType() {
        return type;
    }
}
