package com.endava.mss.serviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endava.mss.constantFiles.LiveConcertConstants;
import com.endava.mss.entities.LiveConcerts;
import com.endava.mss.entityDTO.LiveConcertsDTO;
import com.endava.mss.entityDTO.NotificationDTO;
import com.endava.mss.exception.LiveConcertNotFoundException;
import com.endava.mss.mapper.LiveConcertsMapper;
import com.endava.mss.repository.LiveConcertsRepository;
import com.endava.mss.service.LiveConcertService;
import com.endava.mss.service.NotificationService;

@Service
public class LiveConcertServiceImpl implements LiveConcertService {

	
	private final LiveConcertsRepository liveConcertsRepository;

	private final LiveConcertsMapper liveConcertsMapper;

	private final NotificationService notificationService;
	
	

	public LiveConcertServiceImpl(LiveConcertsRepository liveConcertsRepository, LiveConcertsMapper liveConcertsMapper,
			NotificationService notificationService) {
		
		this.liveConcertsRepository = liveConcertsRepository;
		this.liveConcertsMapper = liveConcertsMapper;
		this.notificationService = notificationService;
	}

	/**
	 * Creates a new live concert and sends notifications to users following the
	 * artist.
	 * 
	 * @param liveConcertDTO The details of the live concert to create.
	 * @return The created live concert details.
	 */
	@Override
	public LiveConcertsDTO newLiveConcert(LiveConcertsDTO liveConcertDTO) {

		LiveConcerts liveConcert = liveConcertsMapper.DTOtoLiveConcerts(liveConcertDTO);

		liveConcertsRepository.save(liveConcert);

		String message = liveConcert.getArtist().getName() + "'s new concert is on " + liveConcert.getDate() + " at "
				+ liveConcert.getLocation();

		liveConcert.getArtist().getUsers().stream().forEach(u -> { // sending notification to all users following artist
			notificationService
					.createNewNotification(NotificationDTO.builder().message(message).userId(u.getId()).build());
		});

		return liveConcertsMapper.liveConcertstoDTO(liveConcert);
	}

	/**
	 * Cancels a live concert and sends cancellation notifications to users.
	 * 
	 * @param liveConcertId The ID of the concert to cancel.
	 */
	@Override
	public void cancelLiveConcert(Long liveConcertId) {

		LiveConcerts liveConcert = liveConcertsRepository.findById(liveConcertId)
				.orElseThrow(() -> new LiveConcertNotFoundException(LiveConcertConstants.CONCERT_NOT_FOUND));

		liveConcertsRepository.deleteById(liveConcertId);

		String message = liveConcert.getArtist().getName() + "'s new concert is on " + liveConcert.getDate() + " at "
				+ liveConcert.getLocation() + " has been canceled";

		liveConcert.getArtist().getUsers().stream().forEach(u -> {
			notificationService
					.createNewNotification(NotificationDTO.builder().message(message).userId(u.getId()).build());
		});

	}

}
