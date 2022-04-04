package com.ejan.assignment.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ejan.assignment.model.User;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query("SELECT u FROM User u WHERE u.email = ?1 and u.isDeleted = false")
	public User getUserByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.isDeleted = false")
	public List<User> getUserList();
	
	@Modifying
	@Query(value = "UPDATE users SET first_name= :firstName, last_name = :lastName, email = :email, role = :role, gender = :gender, phone_number = :phoneNumber, city = :city, address1 = :address1, update_date = :updateDate WHERE user_id = :userId", nativeQuery = true)
	public void updateUser(@Param("userId") long userId, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("email") String email
			, @Param("role") String role, @Param("gender") String gender, @Param("phoneNumber") String phoneNumber, @Param("city") String city, @Param("address1") String address1, @Param("updateDate") Date updateDate);


}
