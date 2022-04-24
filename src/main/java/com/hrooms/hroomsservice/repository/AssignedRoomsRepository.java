package com.hrooms.hroomsservice.repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hrooms.hroomsservice.entities.AssignedRooms;

@Repository
public interface AssignedRoomsRepository extends JpaRepository<AssignedRooms, Integer> {

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update assigenedrooms set provider= :providerId, status= :roomTypeId where id = :assignRoomId")
	void updateProviderForRoom(Integer providerId, int assignRoomId, int roomTypeId);

	@Query(nativeQuery = true, value = "select distinct provider from assigenedrooms where ((fromDate between ?1 and ?2) || (toDate between ?1 and ?2)) and session= ?3")
	public List<Integer> getAssingedProvidersByDateAndSesion(Date fromDate, Date toDate, String session);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "delete from assigenedrooms where ((fromDate between ?2 and ?3) || (toDate between ?2 and ?3)) and roomId = ?1 and session= ?4 order by fromDate")
	public int deleteAssingedRoomsByDateAndSesion(int roomId, Date fromDate, Date toDate, String session);
	
	@Query(nativeQuery = true, value = "select  max(toDate) from assigenedrooms where roomId = ?1")
	public Timestamp getMaxToDateByRoomId(int roomId);
}
