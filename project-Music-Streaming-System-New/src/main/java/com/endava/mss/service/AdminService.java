package com.endava.mss.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.endava.mss.entities.APIGenricResponse;
import com.endava.mss.entities.Admin;
import com.endava.mss.entityDTO.AdminDTO;

public interface AdminService {


	public ResponseEntity<APIGenricResponse>getAdminById(Long Id);
	public ResponseEntity<APIGenricResponse> saveAdmin(AdminDTO adminDTO);
	public ResponseEntity<APIGenricResponse> deleteAdmin(Long id);
	public ResponseEntity<APIGenricResponse>checkLogin(AdminDTO adminDTO);
	public ResponseEntity<APIGenricResponse> getOTP(String email);
	public Optional<Admin> getAdminFromRepo(Long Id);
}
