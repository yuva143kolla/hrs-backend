package com.hrooms.hroomsservice.modal;

import java.util.Date;

import com.hrooms.hroomsservice.Utils.Constants;

public class AuditVO {

	public int id;
	public int roomId; 
	public String roomName;
	public String location;
	public int locationId;
	public Date date; 
	public java.sql.Date dateSql; 
	public String dateStr; 
	public int userId;
	public String user;
	public String action;
	public String comments; 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	} 

	public String getDateStr() {
		if (date != null)
			return Constants.SDF_MM_DD_YYYY_H_M_S.format(date);
		return dateStr;
	} 

	public java.sql.Date getDateSql() {
		if (date != null)
			return new java.sql.Date(date.getTime());
		return dateSql;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setDateSql(java.sql.Date dateSql) {
		this.dateSql = dateSql;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}  	
}
