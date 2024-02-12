package com.ValhallaGains.ValhallaGains.repositories;

import com.ValhallaGains.ValhallaGains.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PurchaseRepository extends JpaRepository<Purchase,Long> {
     boolean existsByNumberOfPurchase(String numberOfPurchase);
}
