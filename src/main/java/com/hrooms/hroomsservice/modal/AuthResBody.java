package com.hrooms.hroomsservice.modal;

import com.hrooms.hroomsservice.entities.User;

public class AuthResBody {
	private boolean authenticated;
	private User user;  

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
