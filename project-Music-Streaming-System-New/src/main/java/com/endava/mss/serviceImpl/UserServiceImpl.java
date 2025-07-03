package com.endava.mss.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.endava.mss.constantFiles.UserConstants;
import com.endava.mss.entities.User;
import com.endava.mss.entityDTO.UserDTO;
import com.endava.mss.entityDTO.UserInfoDTO;
import com.endava.mss.exception.EmailAlreadyExistsException;
import com.endava.mss.exception.EmptyFieldException;
import com.endava.mss.exception.InvalidCredentialsException;
import com.endava.mss.exception.UserNotFoundException;
import com.endava.mss.mapper.UserMapper;
import com.endava.mss.repository.UserRepository;
import com.endava.mss.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final UserMapper userMapper;

	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
	}

	public final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(UserConstants.EMAIL_REGEX,
			Pattern.CASE_INSENSITIVE);

	public boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.matches();
	}

	/**
	 * @return returns all users from the user repository
	 *
	 */
	@Override
	@Transactional
	public List<UserDTO> getAllUsers() {

		return userRepository.findAll().stream().map(p -> userMapper.userToUserDTO(p)).toList();

	}

	/**
	 * @param Id of user
	 * @return returns user object
	 * @throws UserNotFound exception is thrown if the user is not found
	 */
	@Override
	public UserInfoDTO getUserById(Long id) {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(UserConstants.ERROR_USER_NOT_FOUND));
		return userMapper.usertoInfoDTO(user);
	}

	/**
	 * @param User details stored in UserDTO object that needs to be saved in user
	 *             repository
	 * @throws EmailAlreadyExists exception is thrown if the email already exists
	 */
	@Override
	public UserDTO saveUser(UserDTO userDTO) {

		Optional.ofNullable(userDTO.getEmail()).filter(email -> !userRepository.existsByAccount_Email(email))
				.orElseThrow(
						() -> new EmailAlreadyExistsException(UserConstants.ERROR_EMAIL_EXISTS + userDTO.getEmail()));

		String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
		userDTO.setPassword(encodedPassword);

		User user = userMapper.userDTOToUser(userDTO);

		userRepository.save(user);

		return userMapper.userToUserDTO(user);
	}

	/**
	 * @param Id of the user that needs to deleted
	 * @throws UserNotFound exception is thrown if the user is not found
	 */
	@Override
	public void deleteUser(Long id) {

		userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(UserConstants.ERROR_USER_NOT_FOUND));
		userRepository.deleteById(id);

	}

	/**
	 * @param credentials of user stored in userDTO object
	 * @throws EmptyFieldException exception is thrown if email or password is
	 *                             missing
	 * @throws InvalidCredential   exception is thrown if the credentials don't
	 *                             match
	 */
	@Override
	public UserInfoDTO checkLogin(UserDTO userDTO) {

		Optional.ofNullable(userDTO.getEmail()).filter(email -> !email.isEmpty())
				.orElseThrow(() -> new EmptyFieldException(UserConstants.ERROR_EMAIL_REQUIRED));

		Optional.ofNullable(userDTO.getEmail()).filter(this::validate)
				.orElseThrow(() -> new InvalidCredentialsException(UserConstants.ERROR_INVALID_EMAIL));

		Optional.ofNullable(userDTO.getPassword()).filter(password -> !password.isEmpty())
				.orElseThrow(() -> new EmptyFieldException(UserConstants.ERROR_PASSWORD_REQUIRED));

		User storedUser = Optional.ofNullable(userRepository.findByAccount_Email(userDTO.getEmail()))
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		Optional.of(storedUser)
				.filter(user -> passwordEncoder.matches(userDTO.getPassword(), user.getAccount().getPassword()))
				.orElseThrow(() -> new InvalidCredentialsException(UserConstants.INCORRECT_PASSWORD));

		return userMapper.usertoInfoDTO(storedUser);
	}

	

	/**
	 * @param email of the user
	 * @return UserDTO object which corresponds to the email
	 */
	@Override
	public UserInfoDTO getByEmailId(String email) {
		return Optional.ofNullable(userRepository.findByAccount_Email(email)).map(userMapper::usertoInfoDTO)
				.orElseThrow(() -> new UserNotFoundException(UserConstants.ERROR_USER_NOT_FOUND));
	}

	@Override
	public void unfollowArtists(Long userId, Long artistId) {

		userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(UserConstants.ERROR_USER_NOT_FOUND));

		userRepository.unfollowArtist(userId, artistId);

	}

	@Override
	public User findUserById(Long id) {

		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(UserConstants.ERROR_USER_NOT_FOUND));
	}

	/**
	 * @param User Id and current details of user in UserDTO object
	 * @throws UserNotFound exception if the user is not found
	 * @return returns updated UserDTO
	 */
	@Override
	public UserDTO updateUser(Long Id, UserDTO userDTO) {
	    User storedUser = userRepository.findById(Id)
	            .orElseThrow(() -> new UserNotFoundException(UserConstants.ERROR_USER_NOT_FOUND));

	    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
	        if (!passwordEncoder.matches(userDTO.getPassword(), storedUser.getAccount().getPassword())) {
	            storedUser.getAccount().setPassword(passwordEncoder.encode(userDTO.getPassword()));
	        }
	    }

	    if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
	        storedUser.setName(userDTO.getName());
	    }

	    userRepository.save(storedUser);
	    return userMapper.userToUserDTO(storedUser);
	}

}
