package com.example.demo.View;

import com.example.demo.Model.Entities.elem_type;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class SongDTO extends RepresentationModel<SongDTO> {
	private Integer songs_id;
	private String songs_name;
	private String songs_genre;
	private Integer songs_year;
	@Enumerated(EnumType.STRING)
	private elem_type songs_type;
	private Integer album_id;


	public SongDTO(Integer songs_id, String songs_name, String songs_genre, Integer songs_year,elem_type songs_type, Integer album_id) {
		this.songs_id = songs_id;
		this.songs_name = songs_name;
		this.songs_genre = songs_genre;
		this.songs_year = songs_year;
		this.album_id = album_id;
		this.songs_type = songs_type;
	}



	public Integer getSongs_id() {
		return songs_id;
	}

	public void setSongs_id(Integer songs_id) {
		this.songs_id = songs_id;
	}

	public String getSongs_name() {
		return songs_name;
	}

	public void setSongs_name(String songs_name) {
		this.songs_name = songs_name;
	}

	public String getSongs_genre() {
		return songs_genre;
	}

	public void setSongs_genre(String songs_genre) {
		this.songs_genre = songs_genre;
	}

	public Integer getSongs_year() {
		return songs_year;
	}

	public void setSongs_year(Integer songs_year) {
		this.songs_year = songs_year;
	}

	public Integer getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(Integer album_id) {
		this.album_id = album_id;
	}


	public elem_type getSongs_type() {
		return songs_type;
	}

	public void setSongs_type(elem_type songs_type) {
		this.songs_type = songs_type;
	}

	@Override
	public String toString() {
		return "SongDTO{" +
				"songs_id=" + songs_id +
				", songs_name='" + songs_name + '\'' +
				", songs_genre='" + songs_genre + '\'' +
				", songs_year=" + songs_year +
				", songs_type=" + songs_type +
				", album_id=" + album_id +
				'}';
	}
}
