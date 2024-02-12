package com.ValhallaGains.ValhallaGains.repositories;

import com.ValhallaGains.ValhallaGains.models.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DirectionRepository extends JpaRepository<Direction,Long> {

}
