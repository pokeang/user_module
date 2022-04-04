package com.ejan.assignment.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ejan.assignment.dto.UserLoginDTO;
import com.ejan.assignment.dto.UserRegisterDTO;
import com.ejan.assignment.dto.UserUpdateDTO;
import com.ejan.assignment.model.Role;
import com.ejan.assignment.model.User;
import com.ejan.assignment.model.UserLoginHistory;
import com.ejan.assignment.repository.UserLoginHistoryRepository;
import com.ejan.assignment.service.impl.SecurityServiceImpl;
import com.ejan.assignment.service.impl.UserServiceImpl;

@Controller
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserController.class);

	
	@Autowired
	private UserLoginHistoryRepository userLoginHistoryRepo;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private SecurityServiceImpl securityService;

	
	@PostMapping("/login")
	String login(@Valid UserLoginDTO userLoginDTO, BindingResult result, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		if (result.hasErrors()) {
			return "login";
		}
		User user = userService.findUserByEmail(userLoginDTO.getEmail());
		if (user == null) {
			model.addAttribute("error", "Invalid username or password");
			return "login";
		}
		try {
			securityService.autoLogin(userLoginDTO.getEmail(), userLoginDTO.getPassword());
		} catch (Exception e) {
			model.addAttribute("error", "Invalid username or password");
			return "login";
		}
		UserLoginHistory userLoginHistory = new UserLoginHistory(new Date(), request.getRemoteAddr(), user);
		userLoginHistoryRepo.save(userLoginHistory);
		redirectAttrs.addFlashAttribute("currentUserId", user.getUserId());
		return "redirect:/users";
	}
	
	@PostMapping("/adduser")
	public String addUser(@Valid UserRegisterDTO userDTO, BindingResult result, Model model) {
		if (userDTO.getRole() == null) {
			userDTO.setRole(Role.ROLE_USER);
		}
		
		if (result.hasErrors()) {
			return "signup";
		}
		User exitedUser = userService.findUserByEmail(userDTO.getEmail());
		if (exitedUser != null) {
			model.addAttribute("error", "Email address already exited!");
			return "signup";
		}
		
		if (!(userDTO.getPassword().matches(userDTO.getConfirmPassword()))) {
			model.addAttribute("error", "Password not match !");
			return "signup";
		}

		User user = new User();
		BeanUtils.copyProperties(userDTO, user);
		userService.add(user);
		return "redirect:/users";
	}
	
	public String currentUserName() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
	
	@GetMapping("/users")
	public String showUserList(Model model) {
		model.addAttribute("users", userService.getUserList());
		String currentUserLoginEmail = this.currentUserName();
		model.addAttribute("currentUser", currentUserLoginEmail);
		return "user-list";
	}
	
	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		User user = userService.getUserById(id);
		if (user == null) {
			model.addAttribute("error", "Not found user Id:" + id);
			logger.info("Not found user Id:" + id);
			return "user-list";
		}
		UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
		BeanUtils.copyProperties(user, userUpdateDTO);
		userUpdateDTO.setId(id);
		model.addAttribute("userUpdateDTO", userUpdateDTO);
		
		return "update-user";
		
	}
	
	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") long id, @Valid UserUpdateDTO userUpdateDTO, BindingResult result, Model model) {
		if (result.hasErrors()) {
			userUpdateDTO.setId(id);
	    	return "update-user";
	    }
	    User updateUser = new User();
		BeanUtils.copyProperties(userUpdateDTO, updateUser);
		updateUser.setUserId(id);
	    userService.update(updateUser);
	    return "redirect:/users";
	}
	    
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") int id, Model model) {
	    User user = userService.getUserById(id);
	    
	    if (user == null) {
	    	model.addAttribute("error", "Invalid user Id:" + id);
			logger.info("Invalid user Id:" + id);
			return "users";
		}
	    
	    userService.delete(id);
	    return "redirect:/users";
	}

}
