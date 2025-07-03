package com.endava.mss.entityDTO;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsReportDTO {


    private Map<String, Long> genrePopularity;
    private Map<String, Integer> songVerification;
    private Map<String, Integer> userGrowth;
    private Map<String, Integer> releaseTrends;
    private Map<String, Long> playCounts;
    private Map<String, Long> listenedDurations;
}
