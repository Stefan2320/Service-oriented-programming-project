package com.example.demo.View;

import com.example.demo.Model.Entity.InnerPlaylist;
import com.example.demo.Model.Entity.Melodies;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;

public class PlaylistDTO extends RepresentationModel<PlaylistDTO> {


	private String id;
	private String idUser;
	private String name;
	private ArrayList<Melodies> melodii = new ArrayList<Melodies>();
	private ArrayList<InnerPlaylist> playlists = new ArrayList<InnerPlaylist>();

	public PlaylistDTO() {
	}

	public PlaylistDTO(String name,String idUser, ArrayList<Melodies> melodii, ArrayList<InnerPlaylist> playlists) {
		this.name = name;
		this.idUser = idUser;
		this.melodii = melodii;
		this.playlists = playlists;
	}

	public PlaylistDTO(String id,String idUser, String name, ArrayList<Melodies> melodii, ArrayList<InnerPlaylist> playlists) {
		this.id = id;
		this.idUser = idUser;
		this.name = name;
		this.melodii = melodii;
		this.playlists = playlists;
	}

	@Override
	public String toString() {
		return "PlaylistDTO{" +
				"id=" + id +
				", idUser='"+idUser+'\''+
				", name='" + name + '\'' +
				", melodii=" + melodii +
				", playlists=" + playlists +
				'}';
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Melodies> getMelodii() {
		return melodii;
	}

	public void setMelodii(ArrayList<Melodies> melodii) {
		this.melodii = melodii;
	}

	public ArrayList<InnerPlaylist> getPlaylists() {
		return playlists;
	}

	public void setPlaylists(ArrayList<InnerPlaylist> playlists) {
		this.playlists = playlists;
	}
}
