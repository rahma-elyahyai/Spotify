package com.endava.mss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.endava.mss.entities.Admin;
import com.endava.mss.entities.Artist;

import jakarta.transaction.Transactional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>{

	@Modifying
	@Transactional
	@Query(
	    value="UPDATE admin SET name = ?1, updated_at = CURRENT_TIMESTAMP WHERE email = ?2",
	    nativeQuery = true
	)
	int updateAdminNameByEmailId(String name, String emailId);
	
    Admin findByAccount_Email(String email);
    boolean existsByAccount_Email(String email);
}
