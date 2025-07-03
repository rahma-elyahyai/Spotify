package com.endava.mss.constantFiles;

public class UserConstants {

	public static final String USER_BASE_PATH = "/user";
	public static final String USER_BY_ID = "/{userId}";
	public static final String USER_BY_EMAIL = "/";
	public static final String USER_ALL = "/all";
	public static final String USER_UPDATE = "/{userId}";
	public static final String USER_DELETE = "/{userId}";
	public static final String USER_UNFOLLOW_ARTIST = "/{userId}/{artistId}";

	public static final String SUCCESS_DELETE_MESSAGE = "Deleted Successfully";
	public static final String SUCCESS_UNFOLLOW_MESSAGE = "Unfollowed artist";
	
	//error constants
	public static final String ERROR_USER_NOT_FOUND = "User not found";
	public static final String INCORRECT_PASSWORD = "Incorrect password";
    public static final String ERROR_EMAIL_EXISTS = "Email already exists: ";
    public static final String ERROR_EMAIL_REQUIRED = "Email is required.";
    public static final String ERROR_PASSWORD_REQUIRED = "Password is required.";
    public static final String ERROR_INVALID_CREDENTIALS = "Email or password is wrong";
    public static final String ERROR_INVALID_EMAIL = "Invalid email";

   
    public static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
}
