package com.ValhallaGains.ValhallaGains.models;

import jakarta.persistence.*;

@Entity
public class Direction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String county,city;
    private int postalCode,houseNumber;
    private String neighborhood,street;

    @OneToOne
    private Client client;

    public Direction() {
    }

    public Direction(String county, String city, int postalCode, int houseNumber, String neighborhood, String street) {
        this.county = county;
        this.city = city;
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
        this.neighborhood = neighborhood;
        this.street = street;
    }

    public Long getId() {
        return id;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


}
