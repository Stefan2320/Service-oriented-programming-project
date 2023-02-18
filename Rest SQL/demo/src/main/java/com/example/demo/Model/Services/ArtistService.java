package com.example.demo.Model.Services;

import com.example.demo.Model.Entities.Artists;
import com.example.demo.View.ArtistDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistService {



	public List<ArtistDTO> convertToListDTO(List<Artists> artist){

		List<ArtistDTO> artistDTO = new ArrayList<ArtistDTO>();

		for(Artists s: artist){
			artistDTO.add(convertToArtistDTO(s));
		}
		return artistDTO;
	}

	public  ArtistDTO convertToArtistDTO(Artists artist) {
		Integer id = null;

		ArtistDTO artistsDto = new ArtistDTO(artist.getArtists_uuid(), artist.getArtists_name(), artist.getArtists_state());
		return artistsDto;
	}

	public  Artists convertToSongs(ArtistDTO artistDTO) {
		Artists artists = new Artists(artistDTO.getArtists_uuid(), artistDTO.getArtists_name(), artistDTO.getArtists_state());
		return artists;
	}

	public List<ArtistDTO> nameFilter(List<ArtistDTO> DTOartist,String name){

		List<ArtistDTO> DTOsongs2 = new ArrayList<ArtistDTO>();
		for (ArtistDTO s : DTOartist) {
			if (s.getArtists_name().startsWith(name))
				DTOsongs2.add(s);
		}
		if(!name.isEmpty())
			return DTOsongs2;
		else
			return DTOartist;
	}


}
