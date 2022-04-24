package com.hrooms.hroomsservice.entities;


import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="rooms")
public class Rooms {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;  
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location; 
    
    @OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "status", referencedColumnName = "id")
	private RoomStatus roomStatus;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "roomType", referencedColumnName = "id")
	private RoomType roomType;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "roomId", referencedColumnName = "id") 
	private List<AssignedRooms> assignedRooms;
    
    private Date fromDate;
    private Date toDate;
    

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public RoomStatus getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(RoomStatus roomStatus) {
		this.roomStatus = roomStatus;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<AssignedRooms> getAssignedRooms() {
		return assignedRooms;
	}

	public void setAssignedRooms(List<AssignedRooms> assignedRooms) {
		this.assignedRooms = assignedRooms;
	}

	 
}
