package com.endava.mss.serviceImpl;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.endava.mss.constantFiles.AdminConstants;
import com.endava.mss.constantFiles.ArtistConstants;
import com.endava.mss.entities.Artist;
import com.endava.mss.entities.User;
import com.endava.mss.entityDTO.ArtistDTO;
import com.endava.mss.entityDTO.ArtistInfoDTO;
import com.endava.mss.exception.EmailAlreadyExistsException;
import com.endava.mss.exception.EmptyFieldException;
import com.endava.mss.exception.InvalidCredentialsException;
import com.endava.mss.exception.UserNotFoundException;
import com.endava.mss.mapper.ArtistMapper;
import com.endava.mss.repository.ArtistRepository;
import com.endava.mss.repository.UserRepository;
import com.endava.mss.service.ArtistService;

@Service
public class ArtistServiceImpl implements ArtistService {

	private final ArtistRepository artistRepository;

	private final ArtistMapper artistMapper;

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	public ArtistServiceImpl(ArtistRepository artistRepository, ArtistMapper artistMapper,
			PasswordEncoder passwordEncoder, UserRepository userRepository) {

		this.artistRepository = artistRepository;
		this.artistMapper = artistMapper;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	public final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(ArtistConstants.EMAIL_REGEX.getMessage(),
			Pattern.CASE_INSENSITIVE);

	public boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.matches();
	}

	/**
	 * Fetches all the artists from the repository
	 */
	@Override
	public List<ArtistInfoDTO> getAllArtists() {

		List<ArtistInfoDTO> artists = artistRepository.findAll().stream()
				.filter(artist -> artist.getSongs() != null && !artist.getSongs().isEmpty())
				.map(a -> artistMapper.artistToArtistInfo(a)).collect(Collectors.toList());

		return artists;

	}

	/**
	 * This method fetches an artist from the repository by their ID and returns it
	 * as an ArtistDTO.
	 * 
	 * @param id the ID of the artist to be fetched
	 * @return ArtistDTO object of the artist with the specified ID
	 * @throws UserNotFoundException if the artist with the given ID does not exist
	 * 
	 * 
	 */
	@Override
	public ArtistDTO getArtistById(Long id) {
		return artistRepository.findById(id).map(artistMapper::artistToDTO)
				.orElseThrow(() -> new UserNotFoundException(ArtistConstants.ERROR_ARTIST_NOT_FOUND.getMessage()));
	}

	/**
	 * This method saves a new artist to the repository after validating the
	 * required fields (name, profile image). It encodes the password before saving
	 * the artist.
	 * 
	 * 
	 * @param artistDTO the data transfer object containing the artist's details to
	 *                  be saved
	 * @return ArtistDTO object of the saved artist
	 * @throws EmailAlreadyExistsException if the email already exists in the
	 *                                     repository
	 * @throws EmptyFieldException         if required fields (name, profile image)
	 *                                     are missing
	 * 
	 * 
	 */
	@Override
	public ArtistDTO saveArtist(ArtistDTO artistDTO) {

		Optional.ofNullable(artistRepository.existsByAccount_Email(artistDTO.getEmail())).filter(exists -> !exists)
				.orElseThrow(() -> new EmailAlreadyExistsException(
						ArtistConstants.ERROR_EMAIL_ALREADY_EXISTS + artistDTO.getEmail()));

		String encodedPassword = Optional.ofNullable(artistDTO.getPassword()).map(passwordEncoder::encode)
				.orElseThrow(() -> new EmptyFieldException("Password cannot be null"));
		artistDTO.setPassword(encodedPassword);

		Optional.ofNullable(artistDTO.getName()).filter(name -> !name.isEmpty())
				.orElseThrow(() -> new EmptyFieldException(ArtistConstants.ERROR_EMPTY_NAME.getMessage()));

		Optional.ofNullable(artistDTO.getProfileImage())
				.orElseThrow(() -> new EmptyFieldException(ArtistConstants.ERROR_EMPTY_PROFILE_IMAGE.getMessage()));

		Artist artist = artistMapper.DtoToArtist(artistDTO);
		return artistMapper.artistToDTO(artistRepository.save(artist));
	}

	/**
	 * This method checks if the artist's login credentials (email and password) are
	 * valid. If the artist is found and the password matches, an ArtistDTO is
	 * returned.
	 * 
	 * 
	 * @param artistDTO the login credentials of the artist (email and password)
	 * @return ArtistDTO object of the logged-in artist
	 * @throws EmptyFieldException         if the email or password is missing
	 * @throws UserNotFoundException       if the artist with the given email is not
	 *                                     found
	 * @throws InvalidCredentialsException if the email or password is incorrect
	 * 
	 * 
	 */
	@Override
	public ArtistDTO checkLogin(ArtistDTO artistDTO) {

		Optional.ofNullable(artistDTO.getEmail()).filter(email -> !email.isEmpty())
				.orElseThrow(() -> new EmptyFieldException(ArtistConstants.ERROR_EMPTY_EMAIL.getMessage()));

		Optional.ofNullable(artistDTO.getPassword()).filter(password -> !password.isEmpty())
				.orElseThrow(() -> new EmptyFieldException(ArtistConstants.ERROR_EMPTY_PASSWORD.getMessage()));

		Optional.ofNullable(artistDTO.getEmail()).filter(this::validate)
				.orElseThrow(() -> new InvalidCredentialsException(ArtistConstants.ERROR_INVALID_EMAIL.getMessage()));

		Artist storedUser = Optional.ofNullable(artistRepository.findByAccount_Email(artistDTO.getEmail()))
				.orElseThrow(() -> new UserNotFoundException(ArtistConstants.ERROR_USER_NOT_FOUND.getMessage()));

		if (!passwordEncoder.matches(artistDTO.getPassword(), storedUser.getAccount().getPassword())) {
			throw new InvalidCredentialsException(ArtistConstants.ERROR_INVALID_CREDENTIALS.getMessage());
		}

		return artistMapper.artistToDTO(storedUser);
	}

	/**
	 * This method deletes an artist from the repository by their ID. It throws an
	 * exception if the artist is not found.
	 * 
	 * @param id the ID of the artist to be deleted
	 * 
	 */
	@Override
	public void deleteArtist(Long id) {
		if (!artistRepository.existsById(id)) {
			throw new UserNotFoundException(ArtistConstants.ERROR_ARTIST_NOT_FOUND.getMessage());
		}
		artistRepository.deleteById(id);
	}

	/**
	 * Allows a user to follow an artist.
	 * 
	 * @param artistDTO The ArtistDTO object containing artist details.
	 * @param email     The email of the user who wants to follow the artist.
	 * @return ArtistDTO representing the followed artist.
	 * @throws UserNotFoundException if the user with the given email is not found.
	 * @throws RuntimeException      if the user is already following the artist.
	 * 
	 */
	@Override
	public ArtistDTO followArtists(ArtistDTO artistDTO, String userEmail) {

	    User user = Optional.ofNullable( userRepository.findByAccount_Email(userEmail))
	            .orElseThrow(() -> new UserNotFoundException(
	                    ArtistConstants.ERROR_USER_NOT_FOUND_WITH_EMAIL.getMessage() + userEmail));

	    Artist artist = Optional.ofNullable(artistRepository.findByAccount_Email(artistDTO.getEmail()))
	            .orElseThrow(() -> new RuntimeException(AdminConstants.ERROR_CODE_ADMIN_NOT_FOUND+ artistDTO.getEmail()));


	    if (user.getFollowedArtists().contains(artist)) {
	        throw new RuntimeException(ArtistConstants.ERROR_ALREADY_FOLLOWING.getMessage());
	    }

	    user.getFollowedArtists().add(artist);
	    userRepository.save(user);

	    return artistMapper.artistToDTO(artist);
	}


	/**
	 * Checks if a user is following a specific artist.
	 * 
	 * @param artistId  The ID of the artist.
	 * @param userEmail The email of the user.
	 * @return true if the user follows the artist, otherwise false.
	 * @throws UserNotFoundException if the user or artist is not found.
	 * 
	 * 
	 */
	@Override
	public boolean CheckFollowing(Long artistId, String userEmail) {


		User user = Optional.ofNullable(userRepository.findByAccount_Email(userEmail)).orElseThrow(
				() -> new UserNotFoundException(ArtistConstants.ERROR_USER_NOT_FOUND_WITH_EMAIL.getMessage() + userEmail));
		
		Artist artist = artistRepository.findById(artistId)
				.orElseThrow(() -> new UserNotFoundException(ArtistConstants.ERROR_ARTIST_NOT_FOUND.getMessage()));

		return artist.getUsers().contains(user);
	}

	/**
	 * This method retrieves all artists based on their playcount from the
	 * repository and maps them into ArtistDTO objects using the artistMapper, then
	 * returns the list.
	 * 
	 * @return List of all artists in the repository, mapped to ArtistDTO objects
	 * 
	 * 
	 */
	@Override
	public List<ArtistInfoDTO> getTopFiveArtists() {

		return artistRepository.findTopFiveArtists()
        .stream()
        .map(artistMapper::artistToArtistInfo)
        .toList();
	}

	@Override
	public Page<Artist> findNameByIgnoringCase(String term, Pageable pageable) {
		Page<Artist> artistPage = artistRepository.findByNameContainingIgnoreCase(term, pageable);
		return artistPage;
	}

	@Override
	public void saveArtistInRepo(Artist artist) {
	
		 artistRepository.save(artist);
	}

	@Override
	public Optional<Artist> getArtistByIdFromRepo(Long Id) {
		
		return artistRepository.findById(Id);
	}




}
