package com.ValhallaGains.ValhallaGains.dtos;

import com.ValhallaGains.ValhallaGains.models.Direction;

public class DirectionDTO {
    private Long id;
    private String county,city;
    private int postalCode,houseNumber;
    private String neighborhood,street;
    public DirectionDTO(Direction direction){
        id= direction.getId();
        county= direction.getCounty();
        city= direction.getCity();
        postalCode= direction.getPostalCode();
        houseNumber= direction.getHouseNumber();
        neighborhood= direction.getNeighborhood();
        street= direction.getStreet();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}