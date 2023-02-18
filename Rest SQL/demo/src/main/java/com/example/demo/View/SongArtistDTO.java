package com.example.demo.View;

import java.util.ArrayList;

public class SongArtistDTO {

	String song;
	ArrayList<String> artists = new ArrayList<>();

	public SongArtistDTO() {
	}

	public SongArtistDTO(String song) {
		this.song = song;
	}

	public SongArtistDTO(String song, ArrayList<String> artists) {
		this.song = song;
		this.artists = artists;
	}

	public String getSong() {
		return song;
	}

	public void setSong(String song) {
		this.song = song;
	}

	public ArrayList<String> getArtists() {
		return artists;
	}

	public void setArtists(ArrayList<String> artists) {
		this.artists = artists;
	}

	@Override
	public String toString() {
		return "SongArtistDTO{" +
				"song='" + song + '\'' +
				", artists=" + artists +
				'}';
	}

	public void addArtist(String s) {
		this.artists.add(s);
	}
}
