package com.endava.mss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endava.mss.entities.LiveConcerts;

@Repository
public interface LiveConcertsRepository extends JpaRepository<LiveConcerts, Long> {

}
