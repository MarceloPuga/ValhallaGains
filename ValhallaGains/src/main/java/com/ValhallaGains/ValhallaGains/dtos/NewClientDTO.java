package com.ValhallaGains.ValhallaGains.dtos;

import com.ValhallaGains.ValhallaGains.models.Client;
import com.ValhallaGains.ValhallaGains.models.Direction;

public record NewClientDTO(String name, String email, String password, String lastName, String county, String city,
                           Integer postalCode, Integer houseNumber, String neighborhood, String street) {
}
