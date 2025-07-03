package com.endava.mss.service;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.http.ResponseEntity;

import com.endava.mss.entities.APIGenricResponse;

public interface AnalyticsReportService {

	public Map<String, Long> getGenrePopularity();

	public Map<String, Integer> getSongVerificationStatus();

	public TreeMap<String, Integer> getUserGrowth();

	public Map<String, Integer> getReleaseTrends();

	public Map<String, Long> getMostPlayedSongs();

	public Map<String, Long> getListeningDurations();

	public ResponseEntity<APIGenricResponse> getArtistAnalytics(Long artistId);

	public ResponseEntity<APIGenricResponse>getPlayCountsBySong(Long userId);

	public ResponseEntity<APIGenricResponse> getAnalytics();

}
