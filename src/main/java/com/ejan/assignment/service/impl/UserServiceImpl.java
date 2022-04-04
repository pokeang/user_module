package com.ejan.assignment.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ejan.assignment.model.Status;
import com.ejan.assignment.model.User;
import com.ejan.assignment.repository.UserRepository;
import com.ejan.assignment.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	
	@Override
	public List<User> getUserList() {
		logger.info("Start getUserList()");

		List<User> users = userRepository.getUserList();

		logger.info("End getUserList()");
		return users;
	}
	
	@Override
	public List<User> getAllUserList() {
		logger.info("Start getAllUserList()");

		List<User> users = userRepository.findAll();

		logger.info("End getAllUserList()");
		return users;
	}


	@Override
	public boolean delete(long userId) {
		logger.info("Start delete()");
		
		User user = this.getUserById(userId);
		try {
			user.setStatus(Status.DISABLE);
			user.setDeleted(true);
			user.setDeleteDate(new Date());
			userRepository.save(user);
			return true;
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("Start delete()");
		
		return false;
	}

	@Override
	public User update(User user) {
		logger.info("Start update()");
		try {
			user.setUpdateDate(new Date());
			logger.info(user.toString());
			String role = user.getRole().toString();
			String gender = user.getGender().toString();
			userRepository.updateUser(user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmail(),
					role, gender, user.getPhoneNumber(), user.getCity(), user.getAddress1(), user.getUpdateDate());
		
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
		
		logger.info("End update()");
		return user;
	}

	@Override
	public User add(User user) {
		logger.info("Start add()");
		
		user.setCreateDate(new Date());
		String passwordEncode = passwordEncoder.encode(user.getPassword());
		user.setPassword(passwordEncode);
		user.setStatus(Status.ENABLE);
		User savedUser = userRepository.save(user);

		logger.info("End add()");
		return savedUser;
	}

	@Override
	public User getUserById(long userId) {
		logger.info("Start getUserById()");

		User user = userRepository.findById(userId).get();

		logger.info("End getUserById()");
		return user;
	}

	@Override
	public boolean permanentDelete(long userId) {
		logger.info("Start permanentDelete()");
		
		try {
			userRepository.deleteById(userId);
			return true;
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
		
		logger.info("Start permanentDelete()");
		return false;
	}
	
	public User findUserByEmail(String email) {
		logger.info("Start findUserByEmail()");

		User users = userRepository.getUserByEmail(email);

		logger.info("End findUserByEmail()");
		return users;
	}
	

}
