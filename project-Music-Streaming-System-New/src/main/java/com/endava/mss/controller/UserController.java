package com.endava.mss.controller;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.endava.mss.constantFiles.UserConstants;
import com.endava.mss.entityDTO.UserDTO;
import com.endava.mss.entityDTO.UserInfoDTO;
import com.endava.mss.service.UserService;

@RestController
@RequestMapping(UserConstants.USER_BASE_PATH)
public class UserController {


	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping(UserConstants.USER_BY_ID)
	public ResponseEntity<UserInfoDTO> getUserById(@PathVariable Long userId) {
		UserInfoDTO dto = userService.getUserById(userId);

		return new ResponseEntity<>(dto, HttpStatus.OK);

	}

	@GetMapping(UserConstants.USER_BY_EMAIL)
	public ResponseEntity<UserInfoDTO> getUserByEmail(@RequestParam String emailId) {
		UserInfoDTO dto = userService.getByEmailId(emailId);

		return new ResponseEntity<>(dto, HttpStatus.OK);

	}

	@GetMapping(UserConstants.USER_ALL)
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<UserDTO> userDTOs = userService.getAllUsers();
		return new ResponseEntity<>(userDTOs, HttpStatus.OK);

	}

	@PutMapping(path =UserConstants.USER_UPDATE, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO dto) {

		UserDTO userDto = userService.updateUser(userId, dto);

		return new ResponseEntity<>(userDto, HttpStatus.OK);

	}

	@DeleteMapping(UserConstants.USER_DELETE)
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {

		userService.deleteUser(userId);
		return new ResponseEntity<>(UserConstants.SUCCESS_DELETE_MESSAGE, HttpStatus.OK);

	}
	
	@DeleteMapping(UserConstants.USER_UNFOLLOW_ARTIST)
	public ResponseEntity<String> unfollowArtists(@PathVariable Long userId,@PathVariable Long artistId) {

		userService.unfollowArtists(userId, artistId);
		return new ResponseEntity<>(UserConstants.SUCCESS_UNFOLLOW_MESSAGE, HttpStatus.OK);

	}

}
