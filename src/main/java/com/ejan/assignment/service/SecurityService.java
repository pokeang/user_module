package com.ejan.assignment.service;

public interface SecurityService {
	boolean isAuthenticated();
	void autoLogin(String username, String password);
}
