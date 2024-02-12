package com.ValhallaGains.ValhallaGains.dtos;

import com.ValhallaGains.ValhallaGains.models.Client;
import com.ValhallaGains.ValhallaGains.models.Product;
import com.ValhallaGains.ValhallaGains.models.Statustype;

import java.time.LocalDateTime;
import java.util.List;

public record NewPurchaseDTO(LocalDateTime date, Integer totalAmount, List<Integer> quantities, List<String> nombreProductos, List<Integer> unit_price  ) {
}
