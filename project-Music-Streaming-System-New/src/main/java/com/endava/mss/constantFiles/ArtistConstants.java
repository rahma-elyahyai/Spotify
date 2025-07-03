package com.endava.mss.constantFiles;

public enum ArtistConstants {
    

    
    // Messages
    ARTIST_NOT_FOUND("Artist not found"),
    ARTIST_DELETED_SUCCESS("Artist deleted successfully"),
    FOLLOW_SUCCESS("Artist followed successfully"),
    UNFOLLOW_SUCCESS("Artist unfollowed successfully"),
    ERROR_ARTIST_NOT_FOUND("Artist not found"),
    ERROR_EMAIL_ALREADY_EXISTS("Email already exists: "),
    ERROR_EMPTY_NAME("Name is required."),
    ERROR_EMPTY_PROFILE_IMAGE("Profile Image is required."),
    ERROR_EMPTY_EMAIL("Email is required."),
    ERROR_EMPTY_PASSWORD("Password is required."),
    ERROR_INVALID_EMAIL("Invalid Email"),
    ERROR_INVALID_CREDENTIALS("Email or password is wrong"),
    ERROR_USER_NOT_FOUND("User not found"),
    ERROR_USER_NOT_FOUND_WITH_EMAIL("User not found with email: "),
    ERROR_ALREADY_FOLLOWING("You are already following this artist."),
    
    // Regex
    EMAIL_REGEX("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");

    // Static final fields for URL paths
	
    public static final String ARTIST_BASE_URL = "/artist";
    public static final String GET_ALL_ARTISTS = "/all";
    public static final String GET_TOP_ARTISTS = "/topArtists";
    public static final String CHECK_FOLLOWING = "/checkFollowing/{id}/{email}";
    public static final String GET_ARTIST_IMAGE = "/image/{id}";
    public static final String GET_ARTIST_BY_ID = "/{id}";
    public static final String GET_ARTIST_NAME_BY_ID = "/name/{id}";
    public static final String DELETE_ARTIST = "/{id}";
    public static final String FOLLOW_ARTIST =  "/{email}";
    public static final String CONTENT_TYPE_IMAGE ="Content-Type";
    public static final String IMAGE_JPEG="image/jpeg";
    
    private final String value;
   
    ArtistConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
