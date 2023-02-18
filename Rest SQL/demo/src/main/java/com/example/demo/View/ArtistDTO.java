package com.example.demo.View;
import com.example.demo.Model.Entities.Songs;
import org.springframework.hateoas.RepresentationModel;

import java.util.*;

public class ArtistDTO extends RepresentationModel<ArtistDTO> {

	private String artists_uuid;
	private String artists_name;
	private String artists_state;

	Set<Songs> songs = Collections.emptySet();

	List<SongDTO> DTOsongs = new ArrayList<>();

	public List<SongDTO> getDTOsongs() {
		return DTOsongs;
	}

	public void setDTOsongs(List<SongDTO> DTOsongs) {
		this.DTOsongs = DTOsongs;
	}

	public Set<Songs> getSongs() {
		return songs;
	}

	public void setSongs(Set<Songs> songs) {
		this.songs = songs;
	}

	public ArtistDTO(String artists_uuid, String artists_name, String artists_state) {
		this.artists_uuid = artists_uuid;
		this.artists_name = artists_name;
		this.artists_state = artists_state;
	}

	public ArtistDTO(String artists_uuid, String artists_name, String artists_state, Set<Songs> songs) {
		this.artists_uuid = artists_uuid;
		this.artists_name = artists_name;
		this.artists_state = artists_state;
		this.songs = songs;

	}


	public String getArtists_uuid() {
		return artists_uuid;
	}

	public void setArtists_uuid(String artists_uuid) {
		this.artists_uuid = artists_uuid;
	}

	public String getArtists_name() {
		return artists_name;
	}

	public void setArtists_name(String artists_name) {
		this.artists_name = artists_name;
	}

	public String getArtists_state() {
		return artists_state;
	}

	public void setArtists_state(String artists_state) {
		this.artists_state = artists_state;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		ArtistDTO artistDTO = (ArtistDTO) o;
		return Objects.equals(artists_uuid, artistDTO.artists_uuid) && Objects.equals(artists_name, artistDTO.artists_name) && Objects.equals(artists_state, artistDTO.artists_state);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), artists_uuid, artists_name, artists_state);
	}

	@Override
	public String toString() {
		return "ArtistDTO{" +
				"artists_uuid='" + artists_uuid + '\'' +
				", artists_name='" + artists_name + '\'' +
				", artists_state='" + artists_state + '\'' +
				'}';
	}
}
