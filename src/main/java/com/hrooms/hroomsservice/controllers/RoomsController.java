package com.hrooms.hroomsservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrooms.hroomsservice.modal.AssignProvidersVO;
import com.hrooms.hroomsservice.modal.AssignedRoomsVO;
import com.hrooms.hroomsservice.modal.FilterVO;
import com.hrooms.hroomsservice.modal.RoomsVO;
import com.hrooms.hroomsservice.service.RoomService; 

@RestController
@RequestMapping("/room")
@CrossOrigin(origins = "*")
public class RoomsController { 
	@Autowired
	RoomService roomService; 
    
    @GetMapping("/location/{location_ids}")
    public List<RoomsVO> getRoomsByLocation( @PathVariable(value = "location_ids") String locationIds)
    { 
    	return roomService.getRoomsByLocation(locationIds);
    } 
     
    @PostMapping("/assigned")
    public List<AssignedRoomsVO> getAssignedRooms(@RequestBody FilterVO filters)
    { 
    	System.out.println(filters.toString());
    	return roomService.getAllAssignedRooms(filters);
    } 
    
    @PostMapping("/assigne-provider")
    public String assignProvider(@RequestBody AssignedRoomsVO assignedRoomVO)
    { 
    	return roomService.assignProviderToRoom(assignedRoomVO);
    }  
}
