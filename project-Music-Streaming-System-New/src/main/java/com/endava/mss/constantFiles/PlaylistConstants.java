package com.endava.mss.constantFiles;

public class PlaylistConstants {
	
	public static final String BASE_PLAYLIST_URL = "/playlists";
	public static final String ADD_SONG_TO_PLAYLIST = "/songs";
	public static final String FETCH_PLAYLIST = "/{playListId}";
	public static final String FETCH_SHARED_PLAYLIST = "/shared/{playListId}";
	public static final String PLAY_PLAYLIST = "/playPlayList";
	public static final String DELETE_PLAYLIST = "/{playListId}";
	public static final String DELETE_SONG_FROM_PLAYLIST = "/{playListId}/songs/{songId}";
    public static final String SAVE_SHARED_PLAYLIST="/sharedPlaylist/{playListId}/{token}";
	
    public static final String PLAYLIST_NOT_FOUND_ERROR_MESSAGE = "Playlist not found";
    public static final String SONG_NOT_FOUND_ERROR_MESSAGE = "Song not found";
    public static final String SONG_ALREADY_EXISTS_ERROR_MESSAGE = "Song already exists in your playlist";
    public static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found";
    
    public static final String DELETED_PLAYLIST_MESSAGE = "Deleted playlist successfully";
    public static final String DELETED_SONG_FROM_PLAYLIST_MESSAGE = "Deleted song from playlist successfully";

}