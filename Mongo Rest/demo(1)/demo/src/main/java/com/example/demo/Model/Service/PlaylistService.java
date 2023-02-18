package com.example.demo.Model.Service;

import com.example.demo.Model.Entity.Playlist;
import com.example.demo.View.PlaylistDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {


	public PlaylistDTO convertToPlaylistDTO(Playlist playlist) {
		return new PlaylistDTO(playlist.getId(), playlist.getIdUser(), playlist.getName(),playlist.getMelodii(),playlist.getPlaylists());
	}

	public List<PlaylistDTO> convertToPlaylistDTOList(List<Playlist> playlistList) {
		List<PlaylistDTO> listDto = new ArrayList<PlaylistDTO>();
		playlistList.forEach(data -> {
			listDto.add(convertToPlaylistDTO(data));
		});
		return listDto;
	}
}
