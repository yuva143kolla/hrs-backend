package com.hrooms.hroomsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrooms.hroomsservice.entities.RoomStatus; 

@Repository
public interface RoomStatusRepository extends JpaRepository<RoomStatus, Long>{ 
}
