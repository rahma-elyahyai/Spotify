package com.endava.mss.constantFiles;

public class SongQueueConstants {
	
	public static final String SONG_QUEUE_BASE_URL = "/songQueue";
    public static final String ADD_SONG_TO_QUEUE = "/add";
    public static final String GET_USER_SONG_QUEUE = "/{userId}";
    public static final String CLEAR_USER_SONG_QUEUE = "/{userId}";
    public static final String GET_NEXT_SONG= "/next/{userId}";
    public static final String GET_PREVIOUS_SONG = "/previous/{userId}";
    public static final String SHUFFLE_SONGS= "/shuffle/{userId}";
    public static final String GET_ALL_SONGS = "/get/{userId}";
    public static final String REORDER_QUEUE = "/reorder/{userId}";
    public static final String DELETE_SONG_QUEUE = "/song/{songQueueId}";
    
    public static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found";
    public static final String SONG_NOT_FOUND_ERROR_MESSAGE = "Song not found";
    public static final String SONG_QUEUE_NOT_FOUND_ERROR_MESSAGE = "Song queue not found";
    public static final String EMPTY_QUEUE_ERROR_MESSAGE = "Your Queue is empty";
    public static final String SONG_ALREADY_EXISTS_ERROR_MESSAGE = "Song already exists in queue";
    public static final String EMPTY_QUEUE_NO_NEXT_SONG = "No next song available";
    public static final String EMPTY_QUEUE_NO_PREVIOUS_SONG = "No previous song available";
    public static final String DELETION_SUCCESS_MESSAGE = "Deleted Successfully";
    
    public static final String CLEARED_USER_SONG_QUEUE_MESSAGE = "Cleared user's song queue successfully";

    public static final String DELETE_SONG_IN_SONG_QUEUE_MESSAGE = "Deleted song in song queue successfully";
}