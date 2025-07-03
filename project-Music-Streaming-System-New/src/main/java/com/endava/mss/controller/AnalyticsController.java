package com.endava.mss.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endava.mss.constantFiles.AnalyticsReportConstants;
import com.endava.mss.entities.APIGenricResponse;
import com.endava.mss.service.AnalyticsReportService;

@RestController
@RequestMapping(AnalyticsReportConstants.ANALYTICS_PATH)
public class AnalyticsController {

	
	private final AnalyticsReportService analyticsReportService;

	public AnalyticsController(AnalyticsReportService analyticsReportService) {
		this.analyticsReportService = analyticsReportService;
	}

	@GetMapping(AnalyticsReportConstants.ANALYTICS_ADMIN_PATH)
	public  ResponseEntity<APIGenricResponse> getAnalytics() {
		return analyticsReportService.getAnalytics();
	}

	@GetMapping(AnalyticsReportConstants.ANALYTICS_USER_PATH)
	public  ResponseEntity<APIGenricResponse> getUserAnalytics(@PathVariable Long userId) {
		return analyticsReportService.getPlayCountsBySong(userId);
	}

	@GetMapping(AnalyticsReportConstants.ANALYTICS_ARTIST_PATH)
	public ResponseEntity<APIGenricResponse> getArtistAnalytics(@PathVariable Long artistId) {
		return analyticsReportService.getArtistAnalytics(artistId);
	}
}
