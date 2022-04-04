package com.ejan.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejan.assignment.model.UserLoginHistory;

public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Long>{

}
