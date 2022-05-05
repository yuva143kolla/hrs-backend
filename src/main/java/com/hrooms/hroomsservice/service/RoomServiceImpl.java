package com.hrooms.hroomsservice.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrooms.hroomsservice.Utils.Constants;
import com.hrooms.hroomsservice.entities.AssignedRooms;
import com.hrooms.hroomsservice.entities.Audit;
import com.hrooms.hroomsservice.entities.Provider;
import com.hrooms.hroomsservice.entities.RoomStatus;
import com.hrooms.hroomsservice.entities.RoomType;
import com.hrooms.hroomsservice.entities.Rooms;
import com.hrooms.hroomsservice.modal.AssignedRoomsVO;
import com.hrooms.hroomsservice.modal.AuditVO;
import com.hrooms.hroomsservice.modal.FilterVO;
import com.hrooms.hroomsservice.modal.OptionsVO;
import com.hrooms.hroomsservice.modal.RoomsVO;
import com.hrooms.hroomsservice.repository.AssignedRoomsRepository;
import com.hrooms.hroomsservice.repository.AuditRepository;
import com.hrooms.hroomsservice.repository.ProviderRepository;
import com.hrooms.hroomsservice.repository.RoomStatusRepository;
import com.hrooms.hroomsservice.repository.RoomTypeRepository;
import com.hrooms.hroomsservice.repository.RoomsRepository;

@Service
public class RoomServiceImpl implements RoomService {
	@Autowired
	RoomsRepository roomRepository;
	@Autowired
	RoomTypeRepository roomTypeRepo;
	@Autowired
	RoomStatusRepository roomStatusRepo;
	@Autowired
	ProviderRepository providerRepo;
	@Autowired
	AssignedRoomsRepository assignedRoomRepo;
	@Autowired
	AuditRepository auditRepo;

	public static Map<Integer, String> weeks;

	private final String AM = "AM";
	private final String NOON = "Afternoon";
	private final String EVENING = "Evening";

	static {
		weeks = new HashMap<Integer, String>() {
			{
				put(1, "Sun");
				put(2, "Mun");
				put(3, "Tue");
				put(4, "Wed");
				put(5, "Thu");
				put(6, "Fri");
				put(7, "Sat");
			}
		};
	}

	@Override
	public List<OptionsVO> getAllRooms() {
		List<OptionsVO> returnVOs = new ArrayList<OptionsVO>();
		List<Rooms> rooms = roomRepository.findAllOrderByName();
		Set<String> roomNames = new HashSet<>();
		for (Rooms room : rooms) {
			if (roomNames.contains(room.getName())) {
				continue;
			}
			roomNames.add(room.getName());
			OptionsVO roomsVO = new OptionsVO();
			roomsVO.setValue(room.getId());
			roomsVO.setLabel(room.getName());
			returnVOs.add(roomsVO);
		}
		return returnVOs;
	}

	@Override
	public List<RoomsVO> getRoomsByLocation(String locationIds) {
		List<RoomsVO> returnVOs = new ArrayList<RoomsVO>();
		List<Rooms> rooms;
		if (locationIds != null && !locationIds.isEmpty() && locationIds != "null") {
			List<Integer> ids = Stream.of(locationIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			rooms = roomRepository.findByLocationIdIn(ids);

		} else {
			rooms = roomRepository.findAll();
		}
		Set<String> roomNames = new HashSet<>();
		for (Rooms room : rooms) {
			if (roomNames.contains(room.getName())) {
				continue;
			}
			roomNames.add(room.getName());
			RoomsVO roomsVO = new RoomsVO();
			roomsVO.setValue(room.getId());
			roomsVO.setLabel(room.getName());
			returnVOs.add(roomsVO);
		}
		return returnVOs;
	}

	@Override
	public List<OptionsVO> getAllRoomTypes() {
		List<OptionsVO> returnVOs = new ArrayList<OptionsVO>();
		List<RoomType> roomTypes = roomTypeRepo.findAll();
		for (RoomType type : roomTypes) {
			OptionsVO optionVO = new OptionsVO();
			optionVO.setValue(type.getId());
			optionVO.setLabel(type.getType());
			returnVOs.add(optionVO);
		}
		return returnVOs;
	}

	@Override
	public List<OptionsVO> getAllRoomStatus() {
		List<OptionsVO> returnVOs = new ArrayList<OptionsVO>();
		List<RoomStatus> roomStatus = roomStatusRepo.findAll();
		for (RoomStatus status : roomStatus) {
			OptionsVO optionVO = new OptionsVO();
			optionVO.setValue(status.getId());
			optionVO.setLabel(status.getStatus());
			returnVOs.add(optionVO);
		}
		return returnVOs;
	}

	@Override
	public List<OptionsVO> getAllProviders() {
		List<OptionsVO> returnVOs = new ArrayList<OptionsVO>();
		List<Provider> providers = providerRepo.findAll();
		for (Provider provider : providers) {
			OptionsVO optionVO = new OptionsVO();
			optionVO.setValue(provider.getId());
			optionVO.setLabel(provider.getName());
			returnVOs.add(optionVO);
		}
		return returnVOs;
	}

	private Set<String> getRoomNamesByFilter(List<Integer> roomIds) {
		Set<String> roomNames = new HashSet<>();
		if (roomIds != null && !roomIds.isEmpty()) {
			List<Rooms> rooms = roomRepository.findByIdIn(roomIds);
			for (Rooms room : rooms) {
				roomNames.add(room.getName());
			}
		}
		return roomNames;
	}

	@Override
	public List<AssignedRoomsVO> getAllAssignedRooms(FilterVO filters) {

		List<Rooms> assignedRooms = roomRepository.findAll();

		Map<String, Integer> roomStatusMap = roomStatusRepo.findAll().stream()
				.collect(Collectors.toMap(RoomStatus::getStatus, RoomStatus::getId));

		Set<String> roomNames = getRoomNamesByFilter(filters.getRooms());

		Comparator<AssignedRooms> assignedRoomsComparator = Comparator.comparing(AssignedRooms::getSession)
				.thenComparing(r -> r.getProvider().getId()).thenComparing(AssignedRooms::getFromDate);

		List<AssignedRoomsVO> returnList = new ArrayList<>();
		for (Rooms room : assignedRooms) {

			if (filters.getStartDate() != null || filters.getEndDate() != null) {
				if (filters.getStartDate() == null) {
					filters.setStartDate(filters.getEndDate());
				}
				if (filters.getEndDate() == null) {
					filters.setEndDate(filters.getStartDate());
				}

				if (filters.getStartDate().getTime() > room.getFromDate().getTime()) {
					room.setFromDate(filters.getStartDate());
				}
				if (room.getToDate() == null
						|| (room.getToDate() != null && filters.getEndDate().getTime() <= room.getToDate().getTime())) {
					room.setToDate(filters.getEndDate());
				}

				if (!(room.getFromDate().getTime() >= filters.getStartDate().getTime()
						&& room.getFromDate().getTime() <= filters.getEndDate().getTime()))

				{
					continue;
				}
			}

			if (!filters.getRoomTypes().isEmpty()) {
				if (!filters.getRoomTypes().contains(room.getRoomType().getId())) {
					continue;
				}
			}
			if (!filters.getLocations().isEmpty()) {
				if (!filters.getLocations().contains(room.getLocation().getId())) {
					continue;
				}
			}
			if (!roomNames.isEmpty()) {
				if (!roomNames.contains(room.getName())) {
					continue;
				}
			}

			AssignedRoomsVO roomVO_AM = null;
			AssignedRoomsVO roomVO_Noon = null;
			AssignedRoomsVO roomVO_Evening = null;

			if (room.getAssignedRooms() == null || room.getAssignedRooms().isEmpty()) {
				if (room.getToDate() == null) {
					room.setToDate(room.getFromDate());
				}
				if (filters.isShowByDay()) {
					LocalDate startDate = room.getFromDate().toLocalDate();
					LocalDate endDate = room.getToDate().toLocalDate();

					for (LocalDate date = startDate; date.isBefore(endDate)
							|| date.isEqual(endDate); date = date.plusDays(1)) {
						java.util.Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

						roomVO_AM = prepareAssignedRoomVO(room, utilDate, AM);
						roomVO_Noon = prepareAssignedRoomVO(room, utilDate, NOON);
						roomVO_Evening = prepareAssignedRoomVO(room, utilDate, EVENING);

						checAndAddToList(returnList, roomVO_AM, filters, roomStatusMap);
						checAndAddToList(returnList, roomVO_Noon, filters, roomStatusMap);
						checAndAddToList(returnList, roomVO_Evening, filters, roomStatusMap);
					}
				} else {
					roomVO_AM = prepareAssignedRoomVO(room, AM);
					roomVO_Noon = prepareAssignedRoomVO(room, NOON);
					roomVO_Evening = prepareAssignedRoomVO(room, EVENING);

					checAndAddToList(returnList, roomVO_AM, filters, roomStatusMap);
					checAndAddToList(returnList, roomVO_Noon, filters, roomStatusMap);
					checAndAddToList(returnList, roomVO_Evening, filters, roomStatusMap);
				}

			} else {
				LocalDate startDate = room.getFromDate().toLocalDate();
				LocalDate endDate = null;
				if (room.getToDate() == null) {
					Timestamp maxToDate = assignedRoomRepo.getMaxToDateByRoomId(room.getId());
					room.setToDate(maxToDate != null ? new Date(maxToDate.getTime()) : room.getFromDate());
					endDate = maxToDate != null ? new Date(maxToDate.getTime()).toLocalDate()
							: room.getFromDate().toLocalDate();
				} else {
					endDate = room.getToDate().toLocalDate();
				}

				Map<String, AssignedRooms> assignedRoomsMap = new HashMap<>();
				String prevKey = null;
				String prevMapKey = null;

				List<AssignedRooms> assignedRoomsList = new ArrayList<>();
				assignedRoomsList.addAll(room.getAssignedRooms());
				assignedRoomsList.sort(assignedRoomsComparator);

				for (AssignedRooms assigneRoom : assignedRoomsList) {
					if (assigneRoom.getFromDate().getTime() < room.getFromDate().getTime()
							|| assigneRoom.getFromDate().getTime() > room.getToDate().getTime()) {
						continue;
					}
					if (filters.isShowByDay()) {
						assignedRoomsMap.put(assigneRoom.getFromDate().getTime() + "_" + assigneRoom.getSession(),
								assigneRoom);
					} else {
						if (prevKey != null && prevKey
								.equalsIgnoreCase(assigneRoom.getSession() + "_" + assigneRoom.getProvider().getId())) {
							assignedRoomsMap.get(prevMapKey).setToDate(assigneRoom.getFromDate());
						} else {
							prevMapKey = assigneRoom.getFromDate().getTime() + "_" + assigneRoom.getSession();
							assignedRoomsMap.put(prevMapKey, assigneRoom);
						}
						prevKey = assigneRoom.getSession() + "_" + assigneRoom.getProvider().getId();
					}
				}
				Date endDate_AM = null;
				Date endDate_Noon = null;
				Date endDate_Evening = null;

				for (LocalDate date = startDate; date.isBefore(endDate)
						|| date.isEqual(endDate); date = date.plusDays(1)) {
					java.util.Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

					if (endDate_AM == null || (endDate_AM != null && utilDate.getTime() > endDate_AM.getTime())) {
						endDate_AM = null;
						if (assignedRoomsMap.containsKey(utilDate.getTime() + "_" + AM)) {
							if (roomVO_AM != null) {
								roomVO_AM.setEndDate(
										Date.from(date.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
								checAndAddToList(returnList, roomVO_AM, filters, roomStatusMap);
							}

							roomVO_AM = prepareAssignedRoomWithProvider(room,
									assignedRoomsMap.get(utilDate.getTime() + "_" + AM));
							checAndAddToList(returnList, roomVO_AM, filters, roomStatusMap);

							endDate_AM = (Date) roomVO_AM.getEndDate();
							roomVO_AM = null;
						} else if (roomVO_AM == null) {
							roomVO_AM = prepareAssignedRoomVOWithoutEndDate(room, utilDate, AM);
							if (filters.isShowByDay()) {
								roomVO_AM.setEndDate(utilDate);
								checAndAddToList(returnList, roomVO_AM, filters, roomStatusMap);
								roomVO_AM = null;
							}
						}
					}

					if (endDate_Noon == null || (endDate_Noon != null && utilDate.getTime() > endDate_Noon.getTime())) {
						endDate_Noon = null;
						if (assignedRoomsMap.containsKey(utilDate.getTime() + "_" + NOON)) {
							if (roomVO_Noon != null) {
								roomVO_Noon.setEndDate(
										Date.from(date.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
								checAndAddToList(returnList, roomVO_Noon, filters, roomStatusMap);
							}

							roomVO_Noon = prepareAssignedRoomWithProvider(room,
									assignedRoomsMap.get(utilDate.getTime() + "_" + NOON));
							checAndAddToList(returnList, roomVO_Noon, filters, roomStatusMap);

							endDate_Noon = (Date) roomVO_Noon.getEndDate();
							roomVO_Noon = null;
						} else if (roomVO_Noon == null) {
							roomVO_Noon = prepareAssignedRoomVOWithoutEndDate(room, utilDate, NOON);
							if (filters.isShowByDay()) {
								roomVO_Noon.setEndDate(utilDate);
								checAndAddToList(returnList, roomVO_Noon, filters, roomStatusMap);
								roomVO_Noon = null;
							}
						}
					}

					if (endDate_Evening == null
							|| (endDate_Evening != null && utilDate.getTime() > endDate_Evening.getTime())) {
						endDate_Evening = null;
						if (assignedRoomsMap.containsKey(utilDate.getTime() + "_" + EVENING)) {
							if (roomVO_Evening != null) {
								roomVO_Evening.setEndDate(
										Date.from(date.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
								checAndAddToList(returnList, roomVO_Evening, filters, roomStatusMap);
							}

							roomVO_Evening = prepareAssignedRoomWithProvider(room,
									assignedRoomsMap.get(utilDate.getTime() + "_" + EVENING));
							checAndAddToList(returnList, roomVO_Evening, filters, roomStatusMap);

							endDate_Evening = (Date) roomVO_Evening.getEndDate();
							roomVO_Evening = null;
						} else if (roomVO_Evening == null) {
							roomVO_Evening = prepareAssignedRoomVOWithoutEndDate(room, utilDate, EVENING);
							if (filters.isShowByDay()) {
								roomVO_Evening.setEndDate(utilDate);
								checAndAddToList(returnList, roomVO_Evening, filters, roomStatusMap);
								roomVO_Evening = null;
							}
						}
					}
					if (date.isEqual(endDate)) {
						if (roomVO_AM != null) {
							roomVO_AM.setEndDate(utilDate);
							checAndAddToList(returnList, roomVO_AM, filters, roomStatusMap);
						}
						if (roomVO_Noon != null) {
							roomVO_Noon.setEndDate(utilDate);
							checAndAddToList(returnList, roomVO_Noon, filters, roomStatusMap);
						}
						if (roomVO_Evening != null) {
							roomVO_Evening.setEndDate(utilDate);
							checAndAddToList(returnList, roomVO_Evening, filters, roomStatusMap);
						}
					}

				}
			}
		}

		if (!filters.getWeeks().isEmpty()) {
			List<AssignedRoomsVO> weekFilterList = new ArrayList<>();
			for (AssignedRoomsVO assignedRoomsVO : returnList) {
				LocalDate startDate = assignedRoomsVO.getStartDateSql().toLocalDate();
				LocalDate endDate = assignedRoomsVO.getEndDateSql().toLocalDate();

				for (LocalDate date = startDate; date.isBefore(endDate)
						|| date.isEqual(endDate); date = date.plusDays(1)) {
					java.util.Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(utilDate);
					if (filters.getWeeks().contains(weeks.get(calendar.get(Calendar.DAY_OF_WEEK)))) {
						AssignedRoomsVO weekFilterVO = prepareCopy(assignedRoomsVO, utilDate);
						weekFilterList.add(weekFilterVO);
					}

				}

			}
			returnList = new ArrayList<>();
			returnList.addAll(weekFilterList);
		}

		if (!returnList.isEmpty()) {
			Comparator<AssignedRoomsVO> roomsComparator = Comparator.comparing(AssignedRoomsVO::getRoomName)
					.thenComparing(AssignedRoomsVO::getStartDate).thenComparing(AssignedRoomsVO::getEndDate)
					.thenComparing(AssignedRoomsVO::getSessionOrder);
			returnList.sort(roomsComparator);
		}

		return returnList;
	}

	private void checAndAddToList(List<AssignedRoomsVO> returnList, AssignedRoomsVO roomVO, FilterVO filters,
			Map<String, Integer> roomStatusMap) {
		boolean add = true;
		if (filters.getSessions() != null && !filters.getSessions().isEmpty()) {
			if (!filters.getSessions().contains(roomVO.getSession())) {
				add = false;
			}
		}
		if (filters.getProviders() != null && !filters.getProviders().isEmpty()) {
			if (!filters.getProviders().contains(roomVO.getProviderId())) {
				add = false;
			}
		}
		if (!filters.getRoomStatus().isEmpty()) {
			if (!filters.getRoomStatus().contains(roomStatusMap.get(roomVO.getStatus()))) {
				add = false;
			}
		}
		if (add) {
			returnList.add(roomVO);
		}
	}

	private AssignedRoomsVO prepareAssignedRoomVO(Rooms room, String session) {
		AssignedRoomsVO roomVO = new AssignedRoomsVO();
		roomVO.setRoomId(room.getId());
		roomVO.setStartDate(room.getFromDate());
		roomVO.setEndDate(room.getToDate());
		roomVO.setRoomName(room.getName());
		roomVO.setLocation(room.getLocation().getName());
		roomVO.setLocationId(room.getLocation().getId());
		roomVO.setStatus(room.getRoomStatus().getStatus());
		roomVO.setRoomType(room.getRoomType().getType());
		roomVO.setRoomTypeId(room.getRoomType().getId());
		roomVO.setSession(session);
		return roomVO;
	}

	private AssignedRoomsVO prepareAssignedRoomVO(Rooms room, java.util.Date fromDate, String session) {
		AssignedRoomsVO roomVO = new AssignedRoomsVO();
		roomVO.setRoomId(room.getId());
		roomVO.setStartDate(fromDate);
		roomVO.setEndDate(fromDate);
		roomVO.setRoomName(room.getName());
		roomVO.setLocation(room.getLocation().getName());
		roomVO.setLocationId(room.getLocation().getId());
		roomVO.setStatus(room.getRoomStatus().getStatus());
		roomVO.setRoomType(room.getRoomType().getType());
		roomVO.setRoomTypeId(room.getRoomType().getId());
		roomVO.setSession(session);
		return roomVO;
	}

	private AssignedRoomsVO prepareAssignedRoomVOWithoutEndDate(Rooms room, java.util.Date fromDate, String session) {
		AssignedRoomsVO roomVO = new AssignedRoomsVO();
		roomVO.setRoomId(room.getId());
		roomVO.setStartDate(fromDate);
		roomVO.setRoomName(room.getName());
		roomVO.setLocation(room.getLocation().getName());
		roomVO.setLocationId(room.getLocation().getId());
		roomVO.setStatus(room.getRoomStatus().getStatus());
		roomVO.setRoomType(room.getRoomType().getType());
		roomVO.setRoomTypeId(room.getRoomType().getId());
		roomVO.setSession(session);
		return roomVO;
	}

	private AssignedRoomsVO prepareAssignedRoomWithProvider(Rooms room, AssignedRooms assignRoom) {
		AssignedRoomsVO roomVO = new AssignedRoomsVO();
		roomVO.setRoomId(room.getId());
		roomVO.setStartDate(assignRoom.getFromDate());
		roomVO.setEndDate(assignRoom.getToDate());
		if (assignRoom.getToDate().getTime() > room.getToDate().getTime()) {
			roomVO.setEndDate(room.getToDate());
		}
		roomVO.setRoomName(room.getName());
		roomVO.setLocation(room.getLocation().getName());
		roomVO.setLocationId(room.getLocation().getId());
		roomVO.setStatus(Constants.BLOCKED);
		roomVO.setRoomType(room.getRoomType().getType());
		roomVO.setRoomTypeId(room.getRoomType().getId());
		roomVO.setSession(assignRoom.getSession());
		roomVO.setProvider(assignRoom.getProvider().getName());
		roomVO.setProviderId(assignRoom.getProvider().getId());
		roomVO.setAssignRoomId(assignRoom.getId());
		return roomVO;
	}

	private AssignedRoomsVO prepareCopy(AssignedRoomsVO assignRoom, java.util.Date utilDate) {
		AssignedRoomsVO roomVO = new AssignedRoomsVO();
		roomVO.setRoomId(assignRoom.getId());
		roomVO.setStartDate(utilDate);
		roomVO.setEndDate(utilDate);
		roomVO.setRoomName(assignRoom.getRoomName());
		roomVO.setLocation(assignRoom.getLocation());
		roomVO.setLocationId(assignRoom.getLocationId());
		roomVO.setStatus(assignRoom.getStatus());
		roomVO.setRoomType(assignRoom.getRoomType());
		roomVO.setRoomTypeId(assignRoom.getRoomTypeId());
		roomVO.setSession(assignRoom.getSession());
		roomVO.setProvider(assignRoom.getProvider());
		roomVO.setProviderId(assignRoom.getProviderId());
		roomVO.setAssignRoomId(assignRoom.getAssignRoomId());
		return roomVO;
	}

	@Override
	@Transactional
	public String assignProviderToRoom(AssignedRoomsVO assignedRoomVO) {
		Map<String, Integer> roomStatus = roomStatusRepo.findAll().stream()
				.collect(Collectors.toMap(RoomStatus::getStatus, RoomStatus::getId));

		int roomStatusId = roomStatus.get(Constants.OPEN);
		List<AssignedRooms> saveAssignedRooms = new ArrayList<>();
		if (assignedRoomVO.getProviderId() > 0) {
			Optional<Provider> providerById = providerRepo.findById(assignedRoomVO.getProviderId());
			if (providerById.isPresent()) {
				roomStatusId = roomStatus.get(Constants.BLOCKED);

				LocalDate startDate = assignedRoomVO.getStartDateSql().toLocalDate();
				LocalDate endDate = assignedRoomVO.getEndDateSql().toLocalDate();

				for (LocalDate date = startDate; date.isBefore(endDate)
						|| date.isEqual(endDate); date = date.plusDays(1)) {
					AssignedRooms assignRoom = new AssignedRooms();
					assignRoom.setRoomId(assignedRoomVO.getRoomId());
					assignRoom.setFromDate(
							new Date(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()));
					assignRoom.setToDate(
							new Date(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()));
					assignRoom.setSession(assignedRoomVO.getSession());
					assignRoom.setProvider(providerById.get());
					saveAssignedRooms.add(assignRoom);
				}
			}
		}

		assignedRoomRepo.deleteAssingedRoomsByDateAndSesion(assignedRoomVO.getRoomId(),
				assignedRoomVO.getStartDateSql(), assignedRoomVO.getEndDateSql(), assignedRoomVO.getSession());

		if (!saveAssignedRooms.isEmpty()) {
			assignedRoomRepo.saveAll(saveAssignedRooms);
		}

		doAudit(assignedRoomVO);
		// roomRepository.updateRoomStatusByRoomId(assignedRoomVO.getRoomId(),
		// roomStatusId);

		return "DONE";
	}

	private void doAudit(AssignedRoomsVO assignedRoomVO) {
		String action;
		StringBuilder comments = new StringBuilder();
		if (assignedRoomVO.getProviderId() > 0) {
			action = ("Assign Provider");
			Provider provider = providerRepo.getById(assignedRoomVO.getProviderId());
			if (assignedRoomVO.getPrevProvider() != null && !assignedRoomVO.getPrevProvider().isEmpty()) {
				comments.append("Updated Provider ").append("<strong>[").append(assignedRoomVO.getPrevProvider())
						.append(" -> ").append(provider.getName()).append("]</strong>");
			} else { 
				comments.append("Assigned Provider ").append("<strong>[").append(provider.getName())
						.append("]</strong>");
			}
		} else {
			action = ("Remove Provider");
			comments.append("Removed Provider ").append("<strong>[").append(assignedRoomVO.getPrevProvider())
					.append("]</strong>");
		}
		comments.append("<br>");
		comments.append("Dates: ").append(assignedRoomVO.getStartDateStr()).append(" to ")
				.append(assignedRoomVO.getEndDateStr());
		auditRepo.insertAudit(assignedRoomVO.getRoomId(), assignedRoomVO.getLocationId(),
				new java.util.Date(), assignedRoomVO.getUserId(), action, comments.toString());
	}

	@Override
	public List<OptionsVO> getAvilableProviders(AssignedRoomsVO roomVO) {
		List<OptionsVO> returnVOs = new ArrayList<OptionsVO>();
		List<Integer> assignedProviders = assignedRoomRepo.getAssingedProvidersByDateAndSesion(roomVO.getStartDateSql(),
				roomVO.getEndDateSql(), roomVO.getSession());
		List<Provider> providers = providerRepo.findAll();
		for (Provider provider : providers) {
			if (assignedProviders.contains(provider.getId())) {
				if (roomVO.getProviderId() > 0 && roomVO.getProviderId() == provider.getId()) {

				} else {
					continue;
				}
			}
			OptionsVO optionVO = new OptionsVO();
			optionVO.setValue(provider.getId());
			optionVO.setLabel(provider.getName());
			returnVOs.add(optionVO);
		}
		return returnVOs;
	}

	@Override
	public List<AssignedRoomsVO> getManagedRooms(FilterVO filters) {

		List<Rooms> managedRooms = roomRepository.findAll();

		Set<String> roomNames = getRoomNamesByFilter(filters.getRooms());

		Map<String, Integer> roomStatusMap = roomStatusRepo.findAll().stream()
				.collect(Collectors.toMap(RoomStatus::getStatus, RoomStatus::getId));

		List<AssignedRoomsVO> returnList = new ArrayList<>();
		for (Rooms room : managedRooms) {

			if (filters.getStartDate() != null || filters.getEndDate() != null) {
				if (filters.getStartDate() == null) {
					filters.setStartDate(filters.getEndDate());
				}
				if (filters.getEndDate() == null) {
					filters.setEndDate(filters.getStartDate());
				}

				if (filters.getStartDate().getTime() > room.getFromDate().getTime()) {
					room.setFromDate(filters.getStartDate());
				}
				if (room.getToDate() == null
						|| (room.getToDate() != null && filters.getEndDate().getTime() <= room.getToDate().getTime())) {
					room.setToDate(filters.getEndDate());
				}

				if (!(room.getFromDate().getTime() >= filters.getStartDate().getTime()
						&& room.getFromDate().getTime() <= filters.getEndDate().getTime()))

				{
					continue;
				}
			}

			if (!filters.getRoomTypes().isEmpty()) {
				if (!filters.getRoomTypes().contains(room.getRoomType().getId())) {
					continue;
				}
			}
			if (!filters.getLocations().isEmpty()) {
				if (!filters.getLocations().contains(room.getLocation().getId())) {
					continue;
				}
			}
			if (!roomNames.isEmpty()) {
				if (!roomNames.contains(room.getName())) {
					continue;
				}
			}

			AssignedRoomsVO roomVO_AM = null; 
			if (filters.isShowByDay()) {
				LocalDate startDate = room.getFromDate().toLocalDate();
				LocalDate endDate = room.getFromDate().toLocalDate();
				if (room.getToDate() != null) { 
					endDate = room.getToDate().toLocalDate();
				}

				for (LocalDate date = startDate; date.isBefore(endDate)
						|| date.isEqual(endDate); date = date.plusDays(1)) {
					java.util.Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

					roomVO_AM = prepareAssignedRoomVO(room, utilDate, AM);

					checAndAddToList(returnList, roomVO_AM, filters, roomStatusMap);
				}
			} else {
				roomVO_AM = prepareAssignedRoomVO(room, AM);
				checAndAddToList(returnList, roomVO_AM, filters, roomStatusMap);
			}

		}

		if (!filters.getWeeks().isEmpty()) {
			List<AssignedRoomsVO> weekFilterList = new ArrayList<>();
			for (AssignedRoomsVO assignedRoomsVO : returnList) {
				LocalDate startDate = assignedRoomsVO.getStartDateSql().toLocalDate();
				LocalDate endDate = assignedRoomsVO.getEndDateSql().toLocalDate();

				for (LocalDate date = startDate; date.isBefore(endDate)
						|| date.isEqual(endDate); date = date.plusDays(1)) {
					java.util.Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(utilDate);
					if (filters.getWeeks().contains(weeks.get(calendar.get(Calendar.DAY_OF_WEEK)))) {
						AssignedRoomsVO weekFilterVO = prepareCopy(assignedRoomsVO, utilDate);
						weekFilterList.add(weekFilterVO);
					}

				}

			}
			returnList = new ArrayList<>();
			returnList.addAll(weekFilterList);
		}

		if (!returnList.isEmpty()) {
			Comparator<AssignedRoomsVO> roomsComparator = Comparator.comparing(AssignedRoomsVO::getRoomName)
					.thenComparing(AssignedRoomsVO::getStartDate).thenComparing(AssignedRoomsVO::getEndDate)
					.thenComparing(AssignedRoomsVO::getSessionOrder);
			returnList.sort(roomsComparator);
		}

		return returnList;

	}

	@Override
	public List<AuditVO> getAuditDetails(FilterVO filters) {
		List<Audit> auditDetails = auditRepo.findAll();

		Set<String> roomNames = getRoomNamesByFilter(filters.getRooms());

		List<AuditVO> returnList = new ArrayList<>();
		for (Audit room : auditDetails) {

			if (filters.getStartDate() != null || filters.getEndDate() != null) {
				if (filters.getStartDate() == null) {
					filters.setStartDate(filters.getEndDate());
				}
				if (filters.getEndDate() == null) {
					filters.setEndDate(filters.getStartDate());
				}

				if (!(room.getDate().getTime() >= filters.getStartDate().getTime()
						&& room.getDate().getTime() <= filters.getEndDate().getTime()))

				{
					continue;
				}
			}
			if (!filters.getLocations().isEmpty()) {
				if (!filters.getLocations().contains(room.getLocation().getId())) {
					continue;
				}
			}
			if (!roomNames.isEmpty()) {
				if (!roomNames.contains(room.getRoom().getName())) {
					continue;
				}
			}

			AuditVO auditVO = new AuditVO();
			auditVO.setRoomName(room.getRoom().getName());
			auditVO.setLocation(room.getLocation().getName());
			auditVO.setDate(room.getDate());
			auditVO.setUser(room.getUser().getName());
			auditVO.setAction(room.getAction());
			auditVO.setComments(room.getComments());
			returnList.add(auditVO);

			if (!returnList.isEmpty()) {
				Comparator<AuditVO> roomsComparator = Comparator.comparing(AuditVO::getDate).reversed();
				returnList.sort(roomsComparator);
			}

		}
		return returnList;
	}

}
