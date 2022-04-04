package com.ejan.assignment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ejan.assignment.model.User;
import com.ejan.assignment.service.impl.UserServiceImpl;

@RestController
@RequestMapping("api/users")
public class UserApi {
	
	@Autowired
	private UserServiceImpl userService;
	
	@GetMapping
	public List<User> getAllUser() {
		List<User> users = userService.getUserList();
		return users;
	}
	
	@PostMapping
	public String addUser(BindingResult result, @RequestBody @Valid User user) {
//		if (result.hasErrors()) {
//			return result.getAllErrors();
//		}
		userService.add(user);
		return "saved";
	}

}
