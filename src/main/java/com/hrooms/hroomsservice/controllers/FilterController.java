package com.hrooms.hroomsservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrooms.hroomsservice.modal.AssignedRoomsVO;
import com.hrooms.hroomsservice.modal.OptionsVO;
import com.hrooms.hroomsservice.service.RoomService;

@RestController
@RequestMapping("/filter")
@CrossOrigin(origins = "*")
public class FilterController {
	@Autowired
	RoomService roomService;

	@GetMapping("/rooms")
	public List<OptionsVO> getAllRooms() {
		return roomService.getAllRooms();
	}

	@GetMapping("/room-types")
	public List<OptionsVO> getRoomTypes() {
		return roomService.getAllRoomTypes();
	}

	@GetMapping("/room-status")
	public List<OptionsVO> getAllRoomStatus() {
		return roomService.getAllRoomStatus();
	}

	@GetMapping("/providers")
	public List<OptionsVO> getAllProviders() {
		return roomService.getAllProviders();
	}

	@PostMapping("/providers")
	public List<OptionsVO> getAvilableProviders(@RequestBody AssignedRoomsVO roomVO) {
		return roomService.getAvilableProviders(roomVO);
	}
}
