package com.example.demo.Model.Entity;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document("playlist")
public class Playlist {

	@Id
	private String id;

	private String idUser;
	private String name;
	private ArrayList<Melodies> melodii = new ArrayList<Melodies>();
	private ArrayList<InnerPlaylist> playlists = new ArrayList<InnerPlaylist>();

	public Playlist() {
	}

	public Playlist(String name,String idUser, ArrayList<Melodies> melodii, ArrayList<InnerPlaylist> playlists) {
		this.name = name;
		this.idUser = idUser;
		this.melodii = melodii;
		this.playlists = playlists;
	}

	public Playlist(String id,String idUser,String name, ArrayList<Melodies> melodii, ArrayList<InnerPlaylist> playlists) {
		this.id = id;
		this.idUser = idUser;
		this.name = name;
		this.melodii = melodii;
		this.playlists = playlists;
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

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public void setMelodii(ArrayList<Melodies> melodii) {
		this.melodii.addAll(melodii);
		//this.melodii = melodii;
//		this.melodii.add((Melodies) melodii);
	}

	public ArrayList<InnerPlaylist> getPlaylists() {
		return playlists;
	}

	public void setPlaylists(ArrayList<InnerPlaylist> playlists) {
		this.playlists = playlists;
	}

	public void addToPlaylist(InnerPlaylist pl){this.playlists.add(pl);}

	public void addToInnerplaylist(String playlistName,Melodies piesa){
		playlists.forEach(s->{
			if(s.getName().equals(playlistName))
				s.addMelodie(piesa);
		});
	}
	public InnerPlaylist getInnerplaylist(String innerplaylistName){
		InnerPlaylist temporar = new InnerPlaylist();
		playlists.forEach(s->{
			if(s.getName().equals(innerplaylistName))
				BeanUtils.copyProperties(s,temporar);
		});

		return temporar;
	}

	public void deleteInnerPlaylist(InnerPlaylist ip){
		int index = -1;
		for( int i = 0 ; i < playlists.size();i++)
			if(playlists.get(i).getName().equals(ip.getName()))
				index = i;

		if(index != -1) {
			System.out.println("adadas");
			playlists.remove(index);
		}
		else
			System.out.println("EROARE");
	}
	@Override
	public String toString() {
		return "Playlist{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
