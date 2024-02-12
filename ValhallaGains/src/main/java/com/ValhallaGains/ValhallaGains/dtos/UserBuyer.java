package com.ValhallaGains.ValhallaGains.dtos;

import java.util.List;

public record UserBuyer(List<String> title, List<Integer> quantity, List<Integer> unit_price) {
}
