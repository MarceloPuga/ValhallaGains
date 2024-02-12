package com.ValhallaGains.ValhallaGains.repositories;

import com.ValhallaGains.ValhallaGains.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client,Long> {

    Client findByEmail(String email);
    boolean existsByEmail(String name);
    boolean existsByaccessCode(String accessCode);
}
