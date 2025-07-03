package com.endava.mss.constantFiles;

public enum AdminConstants {
    
    // Messages
    ADMIN_NOT_FOUND("Admin not found with ID: "),
    ADMIN_DELETED_SUCCESSFULLY("Deleted Successfully"),
    INVALID_EMAIL("Invalid email address provided"),
    ERROR_EMAIL_ALREADY_EXISTS("Email already exists: "),
    ERROR_EMPTY_EMAIL("Email is required"),
    ERROR_INVALID_EMAIL("Invalid email address provided"),
    ERROR_INVALID_GALAXE_EMAIL("Email should be of Galaxe Solutions"),
    ERROR_USER_NOT_FOUND("Admin not found"),
    ERROR_EMPTY_NAME("Name is required."),
    ERROR_EMPTY_PASSWORD("Password is required."),
    ERROR_INVALID_CREDENTIALS("Password is wrong"),
    ERROR_EMAIL_TOO_LONG("Email is too long"), 
    ERROR_PASSWORD_TOO_LONG("Password too long"),
    OTP_SUBJECT("OTP for Registration"),
	
    // Error Code
    ERROR_CODE_ADMIN_NOT_FOUND("ADMIN_NOT_FOUND"),
    ERROR_CODE_INVALID_EMAIL("INVALID_EMAIL"),
    ERROR_CODE_EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS"),
    ERROR_CODE_EMPTY_EMAIL("EMPTY_EMAIL"),
    ERROR_CODE_EMPTY_NAME("EMPTY_NAME"),
    ERROR_CODE_EMPTY_PASSWORD("EMPTY_PASSWORD"),
    ERROR_CODE_INVALID_CREDENTIALS("INVALID_CREDENTIALS"),
    ERROR_CODE_INVALID_EMAIL_DOMAIN("INVALID EMAIL DOMAIN"),
    ERROR_CODE_USER_NOT_FOUND("USER_NOT_FOUND"),
    
    // Error Details
    ERROR_DETAILS_ADMIN_NOT_FOUND("Please make sure that user has signed up."),
    ERROR_DETAILS_INVALID_EMAIL("Please provide a valid email address."),
    ERROR_DETAILS_EMAIL_ALREADY_EXISTS("This email is already registered."),
    ERROR_DETAILS_EMPTY_EMAIL("Email field cannot be empty."),
    ERROR_DETAILS_EMPTY_NAME("Name cannot be empty."),
    ERROR_DETAILS_EMPTY_PASSWORD("Password cannot be empty."),
    ERROR_DETAILS_INVALID_CREDENTIALS("The password provided is incorrect."),
    ERROR_DETAILS_GALAXE_EMAIL("Please provide a valid @galaxe.com email."),
    ERROR_DETAILS_USER_NOT_FOUND("The admin user with this email was not found."),

    // Success Messages
    SUCCESS("Success"),
    ERROR("Error"),
    
    // Return Messages
    RETURN_ERROR_ADMIN_NOT_FOUND("Admin not found"),
    RETURN_ERROR_INVALID_EMAIL("Invalid email"),
    RETURN_ERROR_INVALID_PASSWORD("Invalid password"),
    RETURN_ERROR_INVALID_CREDENTIALS("Invalid credentials"),
    RETURN_ERROR_INVALID_EMAIL_DOMAIN("Invalid email domain"),
    RETURN_ERROR_EMAIL_ALREADY_EXISTS("Email already exists"),
    RETURN_ERROR_EMPTY_NAME("Admin name is empty"),
    RETURN_ERROR_EMPTY_PASSWORD("Password is empty"),
    RETURN_SUCCESS_ADMIN_FETCH("Admin fetch was successful"),
    RETURN_SUCCESS_ADMIN_DELETED("Admin was successfully deleted"),
    RETURN_SUCCESS_ADMIN_CREATED("Admin created successfully"),
    RETURN_SUCCESS_ADMIN_UPDATED("Admin updated successfully"),
    RETURN_SUCCESS_OTP_GENERATION("OTP generated successfully"),
    RETURN_SUCCESS_LOGIN("Login successfull")
;
    // Regex
    public static final String GALAXE_EMAIL_REGEX = "^[A-Z0-9._%+-]+@galaxe+\\.[A-Z]{2,6}$";

    // Static final fields for URL paths
    public static final String ADMIN_BASE_URL = "/admin";
    public static final String GET_ADMIN_BY_ID = "/get/{id}";
    public static final String GENERATE_OTP = "/OTP";
    public static final String DELETE_ADMIN = "/{userId}";
    
    // OTP range
    public static final int OTP_MIN = 1000;
    public static final int OTP_MAX = 10000;

    private final String value;

    AdminConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
