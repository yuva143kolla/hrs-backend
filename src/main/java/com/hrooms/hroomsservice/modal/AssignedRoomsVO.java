package com.hrooms.hroomsservice.modal;

import java.util.Date;

import com.hrooms.hroomsservice.Utils.Constants;
import com.hrooms.hroomsservice.Utils.SessionEnum;

public class AssignedRoomsVO {

	public int id;
	public int roomId;
	public int assignRoomId;
	public String roomName;
	public String location;
	public Date startDate;
	public Date endDate;
	public java.sql.Date startDateSql;
	public java.sql.Date endDateSql;
	public String startDateStr;
	public String endDateStr;
	public String session;
	public int sessionOrder;
	public String week;
	public int providerId;
	public String provider;
	public String status;
	public String roomType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAssignRoomId() {
		return assignRoomId;
	}

	public void setAssignRoomId(int assignRoomId) {
		this.assignRoomId = assignRoomId;
	} 

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	} 

	public int getSessionOrder() {
		if (session != null)
			return SessionEnum.valueOf(session).getOrder();
		return sessionOrder;
	} 

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getStartDateStr() {
		if (startDate != null)
			return Constants.SDF_MM_DD_YYYY.format(startDate);
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		if (endDate != null)
			return Constants.SDF_MM_DD_YYYY.format(endDate);
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public java.sql.Date getStartDateSql() {
		if (startDate != null)
			return new java.sql.Date(startDate.getTime());
		return startDateSql;
	}

	public java.sql.Date getEndDateSql() {
		if (endDate != null)
			return new java.sql.Date(endDate.getTime());
		return endDateSql;
	}

	@Override
	public String toString() {
		return "AssignedRoomsVO [id=" + id + ", roomName=" + roomName + ", location=" + location + ", startDate="
				+ startDate + ", endDate=" + endDate + ", startDateStr=" + startDateStr + ", endDateStr=" + endDateStr
				+ ", session=" + session + ", week=" + week + ", providerId=" + providerId + ", provider=" + provider
				+ ", status=" + status + ", roomType=" + roomType + "]";
	}

}
