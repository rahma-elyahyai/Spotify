package com.endava.mss.constantFiles;

public enum SongConstants {

	// Success Messages
	SUCCESS("Success"), RETURN_SUCCESS_SONG_FETCH("Song fetched successfully"),
	RETURN_SUCCESS_SONG_CREATED("Song created successfully"),
	RETURN_SUCCESS_SONGS_FETCHED("Songs fetched successfully"),
	RETURN_SUCCESS_SONG_DELETED("Song deleted successfully"), RETURN_SUCCESS_SONG_UPDATED("Song updated successfully"),
	RETURN_SUCCESS_TOP_SONGS_FETCHED("Top songs fetched successfully"),
	RETURN_SUCCESS_PLAY_COUNT_UPDATED("Play count updated successfully"),
	RETURN_SUCCESS_FILTERED_SONGS_FETCHED("Filtered songs fetched successfully"),
	RETURN_SUCCESS_GENRES_FETCHED("Genres fetched successfully"),
	RETURN_SUCCESS_LANGUAGES_FETCHED("Languages fetched successfully"),
	RETURN_SUCCESS_AUTO_SLEEP_COMPLETED("Auto sleep completed successfully"),
	RETURN_SUCCESS_SEARCH_RESULTS_FETCHED("Search results fetched successfully"),
	RETURN_SUCCESS_VOICE_SEARCH_RESULT_FETCHED("Voice search result fetched successfully"),

	// Error Messages
	ERROR_SONG_NOT_FOUND("Song not found"), ERROR_CODE_SONG_NOT_FOUND("SONG_NOT_FOUND"),
	ERROR_DETAILS_SONG_NOT_FOUND("The requested song does not exist in the database."),
	RETURN_ERROR_SONG_NOT_FOUND("Song not found"),

	ERROR_ARTIST_NOT_FOUND("Artist not found"), ERROR_CODE_ARTIST_NOT_FOUND("ARTIST_NOT_FOUND"),
	ERROR_DETAILS_ARTIST_NOT_FOUND("The requested artist does not exist in the database."),
	RETURN_ERROR_ARTIST_NOT_FOUND("Artist not found"),

	ERROR_TITLE_REQUIRED("The title is required."), ERROR_GENRE_REQUIRED("The genre is required."),
	ERROR_LANGUAGE_REQUIRED("The language is required."), ERROR_RELEASE_DATE_REQUIRED("The release date is required."),
	ERROR_COVER_IMAGE_REQUIRED("The cover image is required."), ERROR_MP3_FILE_REQUIRED("The MP3 file is required."),

	ERROR_EMPTY_TITLE("The title is missing or empty."), ERROR_EMPTY_GENRE("The genre is missing or empty."),
	ERROR_EMPTY_LANGUAGE("The language is missing or empty."), ERROR_EMPTY_RELEASE_DATE("The release date is missing."),
	ERROR_EMPTY_COVER_IMAGE("The cover image is missing or empty."),
	ERROR_EMPTY_MP3_FILE("The MP3 file is missing or empty."), ERROR_ADMIN_NOT_FOUND("ARTIST_NOT_FOUND"),

	ARTIST_NOT_FOUND("ARTIST_NOT_FOUND"), ADMIN_NOT_FOUND_MESSAGE("Default admin with ID '1' not found."),
	ADMIN_NOT_FOUND_DETAILS("Please ensure the admin record exists."), EMPTY_TITLE("EMPTY_TITLE"),
	EMPTY_GENRE("EMPTY_GENRE"), EMPTY_LANGUAGE("EMPTY_LANGUAGE"), EMPTY_RELEASE_DATE("EMPTY_RELEASE_DATE"),
	EMPTY_COVER_IMAGE("EMPTY_COVER_IMAGE"), EMPTY_MP3_FILE("EMPTY_MP3_FILE"),
	ARTIST_NOT_FOUND_MESSAGE("Artist is not found"), ARTIST_NOT_FOUND_DETAILS("Selected artist is not found"),
	VALID_TITLE("Please provide a valid title."), VALID_GENRE("Please provide a valid genre."),
	VALID_LANGUAGE("Please provide a valid language."), VALID_RELEASE_DATE("Please provide a valid release date."),
	VALID_COVER_IMAGE("Please provide a valid cover image."), VALID_MP3_FILE("Please provide a valid MP3 file.");

	// Email Notifications
	public static final String EMAIL_SUBJECT_NEW_SONG = "New song release!";
	public static final String EMAIL_BODY_NEW_SONG = """
			<html>
			    <body>
			        <h1>Welcome to Melodify!</h1>
			        <p>We are thrilled to announce that a new song has been released!</p>
			        <p>Check out the latest track by %s!</p>
			        <h3>Listen %s Now!</h3>
			        <p>Enjoy the music!</p>
			    </body>
			</html>
			""";
	public static final String NOTIFICATION_MESSAGE_NEW_SONG = "We are thrilled to announce that a new song has been released!\n"
			+ "Check out the latest track by %s!\n" + "Listen to %s Now!";

	private final String value;

	SongConstants(String value) {
		this.value = value;
	}

	public String getMessage() {
		return value;
	}

	public static final String SONG_BASE_URL = "/artist/songs";
	public static final String STREAM_SONG = "/stream/{id}";
	public static final String GET_SONG_IMAGE = "/image/{id}";
	public static final String CREATE_SONG = "/";
	public static final String UPDATE_SONG = "/";
	public static final String GET_ALL_APPROVED_SONGS = "/all";
	public static final String GET_TOP_FIVE_APPROVED_SONGS = "/approvedSongs";
	public static final String DELETE_SONG = "/{songId}";
	public static final String UPDATE_PLAY_COUNT = "/playCount";
	public static final String FILTER_SONGS = "/filter";
	public static final String GET_GENRES = "/genres";
	public static final String GET_LANGUAGES = "/languages";
	public static final String AUTO_SLEEP = "/autoSleep/{duration}";
	public static final String CONTENT_TYPE_AUDIO = "audio/mp3";
	public static final String CONTENT_TYPE_IMAGE = "image/jpeg";
	public static final String HEADER_CONTENT_RANGE = "Content-Range";
	public static final String SEARCH_SONGS_ARTISTS = "/search/{term}";
	public static final String VOICE_SEARCH_SONGS_ARTISTS = "/voiceSearch/{term}";
	public static final String EDIT_SONGS ="/editSong/{id}";
}
