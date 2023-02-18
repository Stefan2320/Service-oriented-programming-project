package com.example.demo.Model.Entities;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "artists")
public
class Artists {
	private  @Id String artists_uuid;
	private String artists_name;
	private String artists_state;


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "join_songs_artist",
			joinColumns = @JoinColumn(name = "artists_uuid"),
			inverseJoinColumns = @JoinColumn(name = "songs_id"))
	public
	Set<Songs> songs;


	public Set<Songs> getSongs(){
		return songs;
	}
	public void setSong(Songs s){
		songs.add(s);
	}
	public void setSongs(Set<Songs> songs) {
		this.songs = songs;
	}

	public Artists(){}
	public Artists(String artists_id, String artists_name, String artists_state) {
		this.artists_uuid = artists_id;
		this.artists_name = artists_name;
		this.artists_state = artists_state;
	}

	public String getArtists_uuid() {
		return artists_uuid;
	}

	public void setArtists_uuid(String artists_id) {
		this.artists_uuid = artists_id;
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
		Artists artists = (Artists) o;
		return Objects.equals(artists_uuid, artists.artists_uuid) && Objects.equals(artists_name, artists.artists_name) && Objects.equals(artists_state, artists.artists_state);
	}

	@Override
	public String toString() {
		return "Artists{" + "id=" + this.artists_uuid + ", name='" + this.artists_name + '\'' + ", state='" + this.artists_state + '\'' + '}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(artists_uuid, artists_name, artists_state);
	}
}
