package com.endava.mss.controller;

import java.io.IOException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.endava.mss.constantFiles.AuthenticationConstants;
import com.endava.mss.entities.APIGenricResponse;
import com.endava.mss.entities.Artist.isVerified;
import com.endava.mss.entityDTO.AdminDTO;
import com.endava.mss.entityDTO.AdminInfoDTO;
import com.endava.mss.entityDTO.ArtistDTO;
import com.endava.mss.entityDTO.UserDTO;
import com.endava.mss.entityDTO.UserInfoDTO;
import com.endava.mss.service.AdminService;
import com.endava.mss.service.ArtistService;
import com.endava.mss.service.UserService;
import com.endava.mss.serviceImpl.JWTService;
import com.endava.mss.serviceImpl.MyUserDetailsService;

@RestController
@RequestMapping(AuthenticationConstants.AUTHENTICATION_URL)
public class AuthenticationController {

	private final UserService userService;

	private final ArtistService artistService;

	private final AdminService adminService;

	private final JWTService jwtService;

	private final MyUserDetailsService myUserDetailsService;

	public AuthenticationController(UserService userService, ArtistService artistService, AdminService adminService,
			JWTService jwtService, MyUserDetailsService myUserDetailsService) {
		super();
		this.userService = userService;
		this.artistService = artistService;
		this.adminService = adminService;
		this.jwtService = jwtService;
		this.myUserDetailsService = myUserDetailsService;
	}

	@PostMapping(AuthenticationConstants.USER_LOGIN_URL)
	public ResponseEntity<Map<String, Object>> userLogin(@RequestBody UserDTO userDTO) {
		UserInfoDTO authenticatedUser = userService.checkLogin(userDTO);

		UserDetails userDetails = myUserDetailsService.loadUserByUsernameAndRole(authenticatedUser.getEmail(),"ROLE_USER");
		String token = jwtService.generateToken(userDetails);

		return ResponseEntity.ok(Map.of(AuthenticationConstants.TOKEN_KEY, token, AuthenticationConstants.ROLE_KEY,
				jwtService.extractRole(token), AuthenticationConstants.USER_KEY, authenticatedUser));
	}

	@PostMapping(path = AuthenticationConstants.USER_SIGNUP_URL, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDTO> userSignUp(@RequestBody UserDTO dto) {
		UserDTO userDto = userService.saveUser(dto);

		return new ResponseEntity<>(userDto, HttpStatus.CREATED);

	}

	@PostMapping(AuthenticationConstants.ARTIST_LOGIN_URL)
	public ResponseEntity<Map<String, Object>> artistLogin(@RequestBody ArtistDTO artistDTO) {
		ArtistDTO authenticatedArtist = artistService.checkLogin(artistDTO);

		UserDetails userDetails = myUserDetailsService.loadUserByUsernameAndRole(authenticatedArtist.getEmail(),"ROLE_ARTIST");
		String token = jwtService.generateToken(userDetails);

		return ResponseEntity.ok(Map.of(AuthenticationConstants.TOKEN_KEY, token, AuthenticationConstants.ROLE_KEY,
				jwtService.extractRole(token), AuthenticationConstants.ARTIST_KEY, authenticatedArtist));

	}

	@PostMapping(path = AuthenticationConstants.ARTIST_SIGNUP_URL)
	public ArtistDTO artistSignUp(@RequestParam String email, @RequestParam String password, @RequestParam String name,
			@RequestParam String bio, @RequestParam String socialLinks, @RequestParam String status,
			@RequestParam MultipartFile file) {

		ArtistDTO artistDTO = ArtistDTO.builder().bio(bio).email(email).name(name).socialLinks(socialLinks)
				.status(isVerified.PENDING).password(password).build();

		if (file != null && !file.isEmpty()) {

			byte[] profileImageBytes = convertFileToByteArray(file);
			artistDTO.setProfileImage(profileImageBytes);
		}

		return artistService.saveArtist(artistDTO);
	}

	private byte[] convertFileToByteArray(MultipartFile file) {
		try {
			return file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	@PostMapping(AuthenticationConstants.ADMIN_LOGIN_URL)
	public ResponseEntity<?> adminLogin(@RequestBody AdminDTO adminDTO) {
		ResponseEntity<APIGenricResponse> authenticatedAdminResponse = adminService.checkLogin(adminDTO);
	    APIGenricResponse authenticatedAdmin = authenticatedAdminResponse.getBody();
	    AdminInfoDTO fetchedAdminDTO =(AdminInfoDTO) authenticatedAdmin.getBody();
	    if(authenticatedAdmin.getStatus().equals("Error"))
	    {
	    	return authenticatedAdminResponse;
	    }
	    UserDetails userDetails = myUserDetailsService.loadUserByUsernameAndRole(fetchedAdminDTO.email(),"ROLE_ADMIN");
		String token = jwtService.generateToken(userDetails);
		System.out.println(token);
		return ResponseEntity.ok(new APIGenricResponse("Success","Login successful",Map.of(AuthenticationConstants.TOKEN_KEY, token, AuthenticationConstants.ROLE_KEY,
				jwtService.extractRole(token), AuthenticationConstants.ADMIN_KEY, authenticatedAdmin)));
	}

	@PostMapping(path = AuthenticationConstants.ADMIN_SIGNUP_URL, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public void adminSignUp(@RequestBody AdminDTO dto) {
		adminService.saveAdmin(dto);
	}
}
