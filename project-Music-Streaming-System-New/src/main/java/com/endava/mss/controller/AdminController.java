package com.endava.mss.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.endava.mss.constantFiles.AdminConstants;
import com.endava.mss.entities.APIGenricResponse;
import com.endava.mss.service.AdminService;

@RestController
@RequestMapping(AdminConstants.ADMIN_BASE_URL)
public class AdminController {

	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@GetMapping(AdminConstants.GET_ADMIN_BY_ID)
	public ResponseEntity<APIGenricResponse>  getAdminById(@PathVariable Long id) {
		
		return adminService.getAdminById(id);

	}
 
	@PostMapping(AdminConstants.GENERATE_OTP)
	public ResponseEntity<APIGenricResponse>  otpGeneration(@RequestParam String email) {

		return adminService.getOTP(email);
	}

	@DeleteMapping(AdminConstants.DELETE_ADMIN)
	public ResponseEntity<APIGenricResponse>  deleteAdmin(@PathVariable Long userId) {

		return adminService.deleteAdmin(userId);

	}

}
