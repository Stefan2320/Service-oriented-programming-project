package com.example.demo.Model.Services;

import com.example.demo.Model.Entities.Songs;
import com.example.demo.View.SongDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class SongService {


	public List<SongDTO> convertToListDTO(List<Songs> songs){

		List<SongDTO> SongsDTO = new ArrayList<SongDTO>();

		for(Songs s: songs){
			SongsDTO.add(convertToSongDTO(s));
		}
		return SongsDTO;
	}

	public  SongDTO convertToSongDTO(Songs newSong) {
		Integer id = null;
		if(newSong.getAlbum() != null){
			id = newSong.getAlbum().getSongs_id();
		}

		SongDTO songs = new SongDTO(newSong.getSongs_id(), newSong.getSongs_name(), newSong.getSongs_genre(), newSong.getSongs_year(), newSong.getSongs_type(),id);
		return songs;
	}
	public  Songs convertToSongs(SongDTO newSong) {
		Songs songs = new Songs(newSong.getSongs_name(), newSong.getSongs_genre(), newSong.getSongs_year(), newSong.getSongs_type(),null);
		return songs;
	}

	public List<SongDTO> nameFilterMatch(List<SongDTO> DTOsongs,String name){

		List<SongDTO> DTOsongs2 = new ArrayList<SongDTO>();
		for (SongDTO s : DTOsongs) {
			if (s.getSongs_name().equals(name))
				DTOsongs2.add(s);
		}
		if(!name.isEmpty())
			return DTOsongs2;
		else
			return DTOsongs;
	}

	public List<SongDTO> nameFilterNoMatch(List<SongDTO> DTOsongs,String name){

		List<SongDTO> DTOsongs2 = new ArrayList<SongDTO>();
		for (SongDTO s : DTOsongs) {
			if (s.getSongs_name().startsWith(name))
				DTOsongs2.add(s);
		}
		if(!name.isEmpty())
			return DTOsongs2;
		else
			return DTOsongs;
	}

	public List<SongDTO> genreFilter(List<SongDTO> DTOsongs, String genre) {

		List<SongDTO> DTOsongs2 = new ArrayList<SongDTO>();
		for (SongDTO s : DTOsongs) {
			if (s.getSongs_genre().equals(genre))
				DTOsongs2.add(s);
		}
		if(!genre.isEmpty())
			return DTOsongs2;
		else
			return DTOsongs;
	}
	//Default page size = 5
	public List<SongDTO> pageFilterDefault(List<SongDTO> DTOsongs, Integer page,Integer nr_pages,Integer unhandled,Integer... item_per_page) {

		if(item_per_page[0] ==  0)
			item_per_page[0] = 5;

	//	int nr_pages = DTOsongs.size() / item_per_page[0];

		List<SongDTO> DTOsongs2 = new ArrayList<SongDTO>();
		if(page <= nr_pages) {
			for (int i = item_per_page[0] * (page - 1); i < item_per_page[0] * page; i++) {
				DTOsongs2.add(DTOsongs.get(i));
			}
		}else{

			System.out.println("adadsdasda");
			for (int i = DTOsongs.size() - unhandled; i < DTOsongs.size(); i++) {
				DTOsongs2.add(DTOsongs.get(i));
			}
		}



		return DTOsongs2;
	}
}
