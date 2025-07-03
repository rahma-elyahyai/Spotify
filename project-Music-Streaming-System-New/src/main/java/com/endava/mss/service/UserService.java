package com.endava.mss.service;

import java.util.List;

import com.endava.mss.entities.User;
import com.endava.mss.entityDTO.UserDTO;
import com.endava.mss.entityDTO.UserInfoDTO;

public interface UserService {

	public List<UserDTO>getAllUsers();
	public UserInfoDTO getUserById(Long id);
	public UserInfoDTO getByEmailId(String email);
	public UserDTO saveUser(UserDTO userDTO);
	public UserDTO updateUser(Long Id ,UserDTO userDTO);
	public UserInfoDTO checkLogin(UserDTO userDTO);
	public void deleteUser(Long id);
	public void unfollowArtists(Long userId,Long artistId);
	public User findUserById(Long id);
}
