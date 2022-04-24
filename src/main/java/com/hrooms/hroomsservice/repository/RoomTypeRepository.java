package com.hrooms.hroomsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrooms.hroomsservice.entities.RoomType; 

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long>{ 
}
