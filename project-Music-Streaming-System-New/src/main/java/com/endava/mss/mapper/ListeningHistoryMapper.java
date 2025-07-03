package com.endava.mss.mapper;

import org.springframework.stereotype.Component;

import com.endava.mss.entities.ListeningHistory;
import com.endava.mss.entityDTO.ListeningHistoryDTO;

@Component
public class ListeningHistoryMapper {

	public ListeningHistoryDTO ListeningtoDTO(ListeningHistory listeningHistory) {
		if (listeningHistory == null) {
			return null;
		}

		return new ListeningHistoryDTO(listeningHistory.getId(),
				listeningHistory.getSong() != null ? listeningHistory.getSong().getId() : null,
				listeningHistory.getUser() != null ? listeningHistory.getUser().getId() : null,
				listeningHistory.getDurationPlayed() != null ? listeningHistory.getDurationPlayed() : null);
	}

	public ListeningHistory DTOToListeningHistory(ListeningHistoryDTO listeningHistoryDTO) {
		if (listeningHistoryDTO == null) {
			return null;
		}

		ListeningHistory listeningHistory = new ListeningHistory();
		listeningHistory.setId(listeningHistoryDTO.Id());

		return listeningHistory;
	}
}
