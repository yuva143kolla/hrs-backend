package com.hrooms.hroomsservice.modal;

import java.util.ArrayList;
import java.util.List;

public class LocationsVO {
	
	public int value;
	public String label;
	
	public List<RoomsVO> rooms = new ArrayList<RoomsVO>();

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<RoomsVO> getRooms() {
		return rooms;
	}

	public void setRooms(List<RoomsVO> rooms) {
		this.rooms.addAll(rooms);
	} 
	
	public void addRooms(RoomsVO room) {
		this.rooms.add(room);
	} 

}
