package com.hrooms.hroomsservice.service;

import java.util.List;

import com.hrooms.hroomsservice.modal.AssignedRoomsVO;
import com.hrooms.hroomsservice.modal.FilterVO;
import com.hrooms.hroomsservice.modal.OptionsVO;
import com.hrooms.hroomsservice.modal.RoomsVO;

public interface RoomService {
	public List<OptionsVO> getAllRooms();
	
	public List<RoomsVO> getRoomsByLocation (String locationIds);
	
	public List<OptionsVO> getAllRoomTypes();
	
	public List<OptionsVO> getAllRoomStatus();
	
	public List<OptionsVO> getAllProviders();
	
	public List<AssignedRoomsVO> getAllAssignedRooms(FilterVO filters);
	
	public String assignProviderToRoom(AssignedRoomsVO assignedRoomVO);

	public List<OptionsVO> getAvilableProviders(AssignedRoomsVO roomVO);
}
