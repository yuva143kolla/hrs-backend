package com.hrooms.hroomsservice.repository;


import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hrooms.hroomsservice.entities.Audit;


@Repository
public interface AuditRepository extends JpaRepository<Audit, Long>{ 
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO audit (room, location, date, user, action, comments) VALUES (?1,?2,?3,?4,?5,?6)")
	void insertAudit(int room, int location, Date date, int user, String action, String comments);
}
