package com.endava.mss.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.endava.mss.entities.Admin;
import com.endava.mss.entities.AdminPrincipal;
import com.endava.mss.entities.Artist;
import com.endava.mss.entities.ArtistPrincipal;
import com.endava.mss.entities.User;
import com.endava.mss.entities.UserPrincipal;
import com.endava.mss.repository.AdminRepository;
import com.endava.mss.repository.ArtistRepository;
import com.endava.mss.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    
    private final UserRepository userRepository;

    private final ArtistRepository artistRepository;

    private final AdminRepository adminRepository;
    
    

    public MyUserDetailsService(UserRepository userRepository, ArtistRepository artistRepository,
			AdminRepository adminRepository) {
	
		this.userRepository = userRepository;
		this.artistRepository = artistRepository;
		this.adminRepository = adminRepository;
	}



   
    public UserDetails loadUserByUsernameAndRole(String email, String role) throws UsernameNotFoundException {
        if ("ROLE_USER".equalsIgnoreCase(role)) {
            Optional<User> userOpt = Optional.ofNullable(userRepository.findByAccount_Email(email));
            if (userOpt.isPresent()) {
                return new UserPrincipal(userOpt.get());
            }
        }

        if ("ROLE_ARTIST".equalsIgnoreCase(role)) {
            Optional<Artist> artistOpt = Optional.ofNullable(artistRepository.findByAccount_Email(email));
            if (artistOpt.isPresent()) {
                return new ArtistPrincipal(artistOpt.get());
            }
        }

        if ("ROLE_ADMIN".equalsIgnoreCase(role)) {
            Optional<Admin> adminOpt = Optional.ofNullable(adminRepository.findByAccount_Email(email));
            if (adminOpt.isPresent()) {
                return new AdminPrincipal(adminOpt.get());
            }
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }




	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}

}

