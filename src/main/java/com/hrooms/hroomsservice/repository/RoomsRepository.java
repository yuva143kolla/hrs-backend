package com.hrooms.hroomsservice.repository;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hrooms.hroomsservice.entities.Rooms; 

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, Long>{
	
	public List<Rooms> findByLocationIdIn(Collection<Integer> locationIds);
	
	public List<Rooms> findByLocationId(int locationId);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update rooms set status= :statusId where id = :roomId")
	public int updateRoomStatusByRoomId(int roomId, int statusId);
	
	@Query(nativeQuery = true, value = "select * from rooms order by name")
	public List<Rooms> findAllOrderByName();
	 
	public List<Rooms> findByIdIn(Collection<Integer> roomIds);

}
