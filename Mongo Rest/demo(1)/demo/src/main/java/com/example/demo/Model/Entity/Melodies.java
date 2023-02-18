package com.example.demo.Model.Entity;

public class Melodies {

	public String song;
	public String artist;

	String songLink;

	String artistLink;

	public Melodies(String song, String artist, String songLink, String artistLink) {
		this.song = song;
		this.artist = artist;
		this.songLink = songLink;
		this.artistLink = artistLink;
	}

//	public Melodies(String song, String artist) {
//		this.song = song;
//		this.artist = artist;
//	}

	public String getSongLink() {
		return songLink;
	}

	public void setSongLink(String songLink) {
		this.songLink = songLink;
	}

	public String getArtistLink() {
		return artistLink;
	}

	public void setArtistLink(String artistLink) {
		this.artistLink = artistLink;
	}

	public String getSong() {
		return song;
	}

	public void setSong(String song) {
		this.song = song;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
}
