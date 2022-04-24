package com.hrooms.hroomsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrooms.hroomsservice.entities.Location; 

@Repository
public interface LocationRepository extends JpaRepository<Location , Long>{

}
