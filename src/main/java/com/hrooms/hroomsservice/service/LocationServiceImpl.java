package com.hrooms.hroomsservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrooms.hroomsservice.entities.Location;
import com.hrooms.hroomsservice.entities.Rooms;
import com.hrooms.hroomsservice.modal.LocationsVO;
import com.hrooms.hroomsservice.modal.RoomsVO;
import com.hrooms.hroomsservice.repository.LocationRepository;
import com.hrooms.hroomsservice.repository.RoomsRepository;

@Service
public class LocationServiceImpl implements LocationService{
	@Autowired
    LocationRepository locationRepository;
	
	@Autowired
    RoomsRepository roomRepository;
	
	@Override
	public List<LocationsVO> getLocaitonsAndRooms() {
		List<Location> locations = locationRepository.findAll();
		List<LocationsVO> returnVOs = new ArrayList<LocationsVO>();
		for (Location location : locations) {
			LocationsVO locationVO = new LocationsVO();
			locationVO.setValue(location.getId());
			locationVO.setLabel(location.getName());
			List<Rooms> rooms = roomRepository.findByLocationId(location.getId());
			for (Rooms room : rooms) {
				RoomsVO roomsVO = new RoomsVO();
				roomsVO.setValue(room.getId());
				roomsVO.setLabel(room.getName());
				locationVO.addRooms(roomsVO);
			}
			returnVOs.add(locationVO);
		}
		return returnVOs;
	}

}
