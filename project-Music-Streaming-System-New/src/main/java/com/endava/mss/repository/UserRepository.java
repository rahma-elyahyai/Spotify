package com.endava.mss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.endava.mss.entities.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByAccount_Email(String email);
    boolean existsByAccount_Email(String email);
	   
	   @Modifying
	    @Transactional
	    @Query(value = "DELETE FROM user_following_artists WHERE user_id = :userId AND artist_id = :artistId", nativeQuery = true)
	    void unfollowArtist(@Param("userId") Long userId, @Param("artistId") Long artistId);
}
