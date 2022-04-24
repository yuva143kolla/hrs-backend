package com.hrooms.hroomsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrooms.hroomsservice.entities.Provider; 

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer>{ 
}
