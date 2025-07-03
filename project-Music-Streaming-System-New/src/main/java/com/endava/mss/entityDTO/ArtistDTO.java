package com.endava.mss.entityDTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.endava.mss.entities.Album;
import com.endava.mss.entities.Artist.isVerified;

import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArtistDTO {
    private Long id;
    private String name;
    private String email;
    private String password ;
    private String bio;
    private byte[] profileImage;
    private isVerified status;
    private String socialLinks;
    private List<SongInfoDTO> songs;
    private List<AlbumInfoDTO> albums;
    private List<UserInfoDTO> users;
    private Long totalPlayCount;
}
