package com.endava.mss.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.endava.mss.constantFiles.CORSUrl;

@Configuration
@EnableAsync
@EnableWebSecurity
public class WebSecurityConfig {
    
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth

                                .requestMatchers(
                                        "/authentication/**",
                                        "/user/all",
                                        "/artist/all",
                                        "/artist/topArtists",
                                        "/artist/image/**",
                                        "/artist/songs/stream/**",
                                        "/artist/songs/image/**",
                                        "/artist/songs/approvedSongs",
                                        "/artist/songs/all",
                                        "/artist/songs/search/*",
                                        "/album/image/**",
                                        "/artist/songs/filter",
                                        "/artist/songs/genres",
                                        "/artist/songs/languages",
                                        "/artist/{email}",
                                        "/admin/OTP",
                                        "/analytics/*",
                                        "/playlists/shared/*"
                                ).permitAll()


                                .requestMatchers("/user/**", "/playlists/**", "/listeningHistory/**", "/songQueue/**", "/notifications/**")
                                .hasAnyRole("USER", "ADMIN")


                                .requestMatchers("/artist/songs", "/album", "/concerts/**")
                                .hasAnyRole("ARTIST", "ADMIN")


                                .requestMatchers("/admin/**")
                                .hasRole("ADMIN")



                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(CORSUrl.PORT.getMessage()));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
}