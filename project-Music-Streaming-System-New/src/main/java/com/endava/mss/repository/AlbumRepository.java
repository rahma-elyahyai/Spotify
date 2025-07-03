package com.endava.mss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endava.mss.entities.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

}
