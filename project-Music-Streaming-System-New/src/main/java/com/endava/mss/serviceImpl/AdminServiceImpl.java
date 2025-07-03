package com.endava.mss.serviceImpl;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.endava.mss.constantFiles.AdminConstants;
import com.endava.mss.entities.APIGenricResponse;
import com.endava.mss.entities.Admin;
import com.endava.mss.entityDTO.AdminDTO;
import com.endava.mss.entityDTO.AdminInfoDTO;
import com.endava.mss.exception.Exceptions;
import com.endava.mss.mapper.AdminMapper;
import com.endava.mss.repository.AdminRepository;
import com.endava.mss.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	private final AdminRepository adminRepository;

	private final AdminMapper adminMapper;

	private final PasswordEncoder passwordEncoder;

	private final EmailSenderService senderService;

	private final Exceptions adminExceptions;

	public AdminServiceImpl(AdminRepository adminRepository, AdminMapper adminMapper, PasswordEncoder passwordEncoder,
			EmailSenderService senderService, Exceptions adminExceptions) {

		this.adminRepository = adminRepository;
		this.adminMapper = adminMapper;
		this.passwordEncoder = passwordEncoder;
		this.senderService = senderService;
		this.adminExceptions = adminExceptions;
	}

	public final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(AdminConstants.GALAXE_EMAIL_REGEX,
			Pattern.CASE_INSENSITIVE);

	public boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.matches();
	}

	/**
	 * Retrieves admin details based on their ID.
	 *
	 * @param id Unique identifier of the admin.
	 * @return ResponseEntity containing: -The admin details if successful -Error
	 *         response if admin is not found
	 */
	@Override
	public ResponseEntity<APIGenricResponse> getAdminById(Long id) {

		Optional<Admin> adminOptional = adminRepository.findById(id);
		if (adminOptional.isEmpty()) {
			return adminExceptions.RecordNotFound(AdminConstants.ERROR_CODE_ADMIN_NOT_FOUND.getMessage(),
					AdminConstants.ADMIN_NOT_FOUND.getMessage(),
					AdminConstants.ERROR_DETAILS_ADMIN_NOT_FOUND.getMessage(),
					AdminConstants.RETURN_ERROR_ADMIN_NOT_FOUND.getMessage());
		}
		AdminDTO adminDTO = adminMapper.AdmintoAdminDTO(adminOptional.get());
		APIGenricResponse response = new APIGenricResponse(AdminConstants.SUCCESS.getMessage(),
				AdminConstants.RETURN_SUCCESS_ADMIN_FETCH.getMessage(), adminDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Sends an email with a given subject and body.
	 *
	 * @param email   Recipient email address.
	 * @param subject Email subject.
	 * @param body    Email content.
	 */
	public void sendMail(String email, String subject, String body) {
		senderService.sendEmail(email, subject, body);
	}

	/**
	 * Generates and sends a One-Time Password (OTP) to the given email.
	 *
	 * @param email Recipient email address.
	 * @return ResponseEntity containing: - The generated OTP if the request is
	 *         successful. - An error response if the email is empty. - An error
	 *         response if the email already exists in the repository. - An error
	 *         response if the email does not belong to the '@galaxe.com' domain.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> getOTP(String email) {

		Optional<String> emailOptional = Optional.ofNullable(email).filter(e -> !e.isEmpty());
		if (emailOptional.isEmpty()) {
			return adminExceptions.EmptyField(AdminConstants.ERROR_CODE_EMPTY_EMAIL.getMessage(),
					AdminConstants.ERROR_EMPTY_EMAIL.getMessage(),
					AdminConstants.ERROR_DETAILS_EMPTY_EMAIL.getMessage(),
					AdminConstants.RETURN_ERROR_INVALID_EMAIL.getMessage());
		}
		if (adminRepository.existsByAccount_Email(emailOptional.get())) {
			return adminExceptions.EntityAlreadyExists(AdminConstants.ERROR_CODE_EMAIL_ALREADY_EXISTS.getMessage(),
					AdminConstants.ERROR_EMAIL_ALREADY_EXISTS.getMessage(),
					AdminConstants.ERROR_DETAILS_EMAIL_ALREADY_EXISTS.getMessage(),
					AdminConstants.RETURN_ERROR_EMAIL_ALREADY_EXISTS.getMessage());
		}
		if (!emailOptional.get().contains("@galaxe.com")) {
			return adminExceptions.InvalidEmailDomain(AdminConstants.ERROR_CODE_INVALID_EMAIL_DOMAIN.getMessage(),
					AdminConstants.ERROR_INVALID_GALAXE_EMAIL.getMessage(),
					AdminConstants.ERROR_DETAILS_GALAXE_EMAIL.getMessage(),
					AdminConstants.RETURN_ERROR_INVALID_EMAIL_DOMAIN.getMessage());
		}
		Long OTP = ThreadLocalRandom.current().nextLong(1000, 10000); // Generates random number between 1000 and 10000
		sendMail(emailOptional.get(), AdminConstants.OTP_SUBJECT.getMessage(), String.valueOf(OTP));

		APIGenricResponse response = new APIGenricResponse(AdminConstants.SUCCESS.getMessage(),
				AdminConstants.RETURN_SUCCESS_OTP_GENERATION.getMessage(), OTP);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Saves a new admin after validating the required fields.
	 * 
	 * @param adminDTO The AdminDTO object containing the admin's details to be
	 *                 saved.
	 * @return ResponseEntity containing: - The saved admin details if the operation
	 *         is successful. - An error response if the email already exists in the
	 *         repository. - An error response if the name field is empty. - An
	 *         error response if the password field is empty.
	 */

	@Override
	public ResponseEntity<APIGenricResponse> saveAdmin(AdminDTO adminDTO) {

		System.out.println(adminDTO);

		if (adminRepository.existsByAccount_Email(adminDTO.getEmail())) {
			return adminExceptions.EntityAlreadyExists(AdminConstants.ERROR_CODE_EMAIL_ALREADY_EXISTS.getMessage(),
					AdminConstants.ERROR_EMAIL_ALREADY_EXISTS.getMessage(),
					AdminConstants.ERROR_DETAILS_EMAIL_ALREADY_EXISTS.getMessage(),
					AdminConstants.RETURN_ERROR_EMAIL_ALREADY_EXISTS.getMessage());
		}
		if (adminDTO.getName() == null || adminDTO.getName().trim().isEmpty()) {
			return adminExceptions.EmptyField(AdminConstants.ERROR_DETAILS_EMPTY_NAME.getMessage(),
					AdminConstants.ERROR_EMPTY_NAME.getMessage(), AdminConstants.ERROR_DETAILS_EMPTY_NAME.getMessage(),
					AdminConstants.RETURN_ERROR_EMPTY_NAME.getMessage());
		}

		if (adminDTO.getPassword() == null || adminDTO.getPassword().trim().isEmpty()) {
			return adminExceptions.EmptyField(AdminConstants.ERROR_DETAILS_EMPTY_PASSWORD.getMessage(),
					AdminConstants.ERROR_EMPTY_PASSWORD.getMessage(),
					AdminConstants.ERROR_DETAILS_EMPTY_PASSWORD.getMessage(),
					AdminConstants.RETURN_ERROR_EMPTY_PASSWORD.getMessage());
		}


		String encodedPassword = passwordEncoder.encode(adminDTO.getPassword());
		adminDTO.setPassword(encodedPassword);

		Admin admin = adminMapper.AdminDTOtoAdminEntity(adminDTO);
		Admin savedAdmin = adminRepository.save(admin);
		AdminDTO savedAdminDTO = adminMapper.AdmintoAdminDTO(savedAdmin);

		APIGenricResponse response = new APIGenricResponse(AdminConstants.SUCCESS.getMessage(),
				AdminConstants.RETURN_SUCCESS_ADMIN_CREATED.getMessage(), savedAdminDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Deletes an admin from the database.
	 *
	 * @param id Unique identifier of the admin.
	 * @return ResponseEntity indicating success or failure of deletion.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> deleteAdmin(Long id) {

		Optional<Admin> adminOptional = adminRepository.findById(id);
		if (adminOptional.isEmpty()) {
			return adminExceptions.RecordNotFound(AdminConstants.ERROR_CODE_ADMIN_NOT_FOUND.getMessage(),
					AdminConstants.ADMIN_NOT_FOUND.getMessage(),
					AdminConstants.ERROR_DETAILS_ADMIN_NOT_FOUND.getMessage(),
					AdminConstants.RETURN_ERROR_ADMIN_NOT_FOUND.getMessage());
		}

		adminRepository.deleteById(id);

		APIGenricResponse response = new APIGenricResponse(AdminConstants.SUCCESS.getMessage(),
				AdminConstants.ADMIN_DELETED_SUCCESSFULLY.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Validates the login credentials of an admin.
	 * 
	 * 
	 * @param adminDTO The AdminDTO object containing the email and password of the
	 *                 admin.
	 * @return ResponseEntity containing: - A success response with the admin
	 *         details if authentication is successful. - An error response if the
	 *         email field is empty. - An error response if the password field is
	 *         empty. - An error response if the email format is invalid or does not
	 *         belong to "galaxe.com". - An error response if no admin record is
	 *         found for the given email. - An error response if the provided
	 *         password does not match the stored password.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> checkLogin(AdminDTO adminDTO) {

		Optional<String> emailOptional = Optional.ofNullable(adminDTO.getEmail()).filter(email -> !email.isEmpty());

		if (emailOptional.isEmpty()) {
			return adminExceptions.EmptyField(AdminConstants.ERROR_CODE_EMPTY_EMAIL.getMessage(),
					AdminConstants.ERROR_EMPTY_EMAIL.getMessage(),
					AdminConstants.ERROR_DETAILS_EMPTY_EMAIL.getMessage(),
					AdminConstants.RETURN_ERROR_INVALID_EMAIL.getMessage());
		}

		Optional<String> passwordOptional = Optional.ofNullable(adminDTO.getPassword())
				.filter(password -> !password.isEmpty());

		if (passwordOptional.isEmpty()) {
			return adminExceptions.EmptyField(AdminConstants.ERROR_CODE_EMPTY_PASSWORD.getMessage(),
					AdminConstants.ERROR_EMPTY_PASSWORD.getMessage(),
					AdminConstants.ERROR_DETAILS_EMPTY_PASSWORD.getMessage(),
					AdminConstants.RETURN_ERROR_EMPTY_PASSWORD.getMessage());
		}

		if (!validate(adminDTO.getEmail())) {
			String errorMessage = adminDTO.getEmail().contains("galaxe")
					? AdminConstants.ERROR_INVALID_EMAIL.getMessage()
					: AdminConstants.ERROR_INVALID_GALAXE_EMAIL.getMessage();
			return adminExceptions.InvalidEmail(AdminConstants.INVALID_EMAIL.getMessage(), errorMessage,
					AdminConstants.ERROR_DETAILS_INVALID_EMAIL.getMessage(),
					AdminConstants.RETURN_ERROR_INVALID_EMAIL.getMessage());
		}

		Optional<Admin> storedUserOptional = Optional
				.ofNullable(adminRepository.findByAccount_Email(adminDTO.getEmail()));

		if (storedUserOptional.isEmpty()) {
			return adminExceptions.RecordNotFound(AdminConstants.ERROR_CODE_ADMIN_NOT_FOUND.getMessage(),
					AdminConstants.ADMIN_NOT_FOUND.getMessage(),
					AdminConstants.ERROR_DETAILS_ADMIN_NOT_FOUND.getMessage(),
					AdminConstants.RETURN_ERROR_ADMIN_NOT_FOUND.getMessage());
		}

		Admin storedUser = storedUserOptional.get();

		if (!passwordEncoder.matches(adminDTO.getPassword(), storedUser.getAccount().getPassword())) {
			return adminExceptions.InvalidCredentials(AdminConstants.ERROR_CODE_INVALID_CREDENTIALS.getMessage(),
					AdminConstants.ERROR_INVALID_CREDENTIALS.getMessage(),
					AdminConstants.ERROR_DETAILS_INVALID_CREDENTIALS.getMessage(),
					AdminConstants.RETURN_ERROR_INVALID_CREDENTIALS.getMessage());
		}

		AdminInfoDTO userDTO = adminMapper.adminToAdminInfoDTO(storedUser);
		APIGenricResponse response = new APIGenricResponse(AdminConstants.SUCCESS.getMessage(),
				AdminConstants.RETURN_SUCCESS_LOGIN.getMessage(), userDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public Optional<Admin> getAdminFromRepo(Long Id) {

		return adminRepository.findById(Id);
	}

}
