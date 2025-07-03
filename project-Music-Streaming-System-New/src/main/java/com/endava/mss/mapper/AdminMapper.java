package com.endava.mss.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.cache.spi.support.NaturalIdNonStrictReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endava.mss.entities.Account;
import com.endava.mss.entities.Admin;
import com.endava.mss.entityDTO.AdminDTO;
import com.endava.mss.entityDTO.AdminInfoDTO;
import com.endava.mss.entityDTO.SongInfoDTO;

@Component
public class AdminMapper {

	@Autowired
	SongMapper songMapper;

	public AdminDTO AdmintoAdminDTO(Admin admin) {
		if (admin == null) {
			return null;
		}
		List<SongInfoDTO> songInfoDTOs = admin.getSong() != null
				? admin.getSong().stream().map(p -> songMapper.songtoSongInfo(p)).collect(Collectors.toList())
				: null;

		return AdminDTO.builder().Id(admin.getId()).name(admin.getName()).email(admin.getAccount().getEmail())
				.password(admin.getAccount().getPassword()).createdAt(admin.getAccount().getCreatedAt())
				.updatedAt(admin.getAccount().getUpdatedAt()).song(songInfoDTOs).build();
	}

	public Admin AdminDTOtoAdminEntity(AdminDTO adminDTO) {
		if (adminDTO == null) {
			return null;
		}
		return Admin.builder().Id(adminDTO.getId()).name(adminDTO.getName())
				.account(Account.builder().email(adminDTO.getEmail()).password(adminDTO.getPassword())
						.createdAt(adminDTO.getCreatedAt()).updatedAt(adminDTO.getUpdatedAt()).build())
				.build();
	}
	
	public AdminInfoDTO adminToAdminInfoDTO(Admin admin)
	{
		return new AdminInfoDTO(admin.getId(), admin.getName(), admin.getAccount().getEmail());
	}
}
