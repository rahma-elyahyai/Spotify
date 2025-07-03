package com.endava.mss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endava.mss.entities.AnalyticsReport;

@Repository
public interface AnalyticsReportRepository extends JpaRepository<AnalyticsReport, Long> {

}
