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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrooms.hroomsservice.modal.AssignProvidersVO;
import com.hrooms.hroomsservice.modal.AssignedRoomsVO;
import com.hrooms.hroomsservice.modal.AuditVO;
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
    	return roomService.getAllAssignedRooms(filters);
    } 
    
    @PostMapping("/managed")
    public List<AssignedRoomsVO> getManagedRooms(@RequestBody FilterVO filters)
    { 
    	return roomService.getManagedRooms(filters);
    } 
    
    @PostMapping("/audit")
    public List<AuditVO> getAuditDetails(@RequestBody FilterVO filters)
    { 
    	return roomService.getAuditDetails(filters);
    } 
    
    @PostMapping("/assigne-provider")
    @JsonFormat(timezone="")
    public String assignProvider(@RequestBody AssignedRoomsVO assignedRoomVO)
    { 
    	return roomService.assignProviderToRoom(assignedRoomVO);
    }  
}
