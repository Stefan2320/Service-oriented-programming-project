package com.example.demo.Model.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.*;

import javax.persistence.*;


@Entity
@Table(name = "songs")
public
class Songs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer songs_id;
    private String songs_name;
    private String songs_genre;
    private Integer songs_year;

    private Integer album_id;


    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE )
    @JoinColumn(name="album_id",insertable=false,updatable = false)
    private Songs album;


    @OneToMany(mappedBy = "album",fetch = FetchType.EAGER)
    @Transient
    private Set<Songs> albumSongs = new HashSet<Songs>();

    @Enumerated(EnumType.STRING)
    private elem_type songs_type;
    // eager: automatically pull back child entities.
    @ManyToMany(fetch=FetchType.EAGER ,mappedBy = "songs")
    Set<Artists> artists;

    Set<Artists> artists(){
        return artists;
    }
    Songs() {}


    public Songs(String songs_name, String songs_genre, Integer songs_year, elem_type songs_type, Songs album_id){
        this.songs_name = songs_name;
        this.songs_genre = songs_genre;
        this.songs_year = songs_year;
        this.songs_type =  songs_type;
        this.album= album_id;
    }



    @Override
    public int hashCode() {return Objects.hash(this.songs_id, this.songs_name, this.songs_genre, this.songs_year, this.songs_type);}

    public void setAlbum(Songs album_id) {this.album = album_id;}

    public Songs getAlbum() {
        return album;
    }

    public Integer getSongs_id(){ return this.songs_id;}
    public String getSongs_name(){return this.songs_name;}
    public String getSongs_genre(){return this.songs_genre;}
    public Integer getSongs_year(){return this.songs_year;}
    public elem_type getSongs_type(){return this.songs_type;}

    public Integer getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(Integer album_id) {
        this.album_id = album_id;
    }

    public void setSongs_id(Integer songs_id){ this.songs_id = songs_id;}
    public void setSongs_name(String songs_name){this.songs_name =  songs_name;}
    public void setSongs_genre(String songs_genre){ this.songs_genre = songs_genre;}
    public void setSongs_year(Integer songs_year){this.songs_year = songs_year;}
    public void setSongs_type(elem_type songs_type){this.songs_type = songs_type;}

    @Override
    public String toString() {
        return "Songs{" +
                "songs_id=" + songs_id +
                ", songs_name='" + songs_name + '\'' +
                ", songs_genre='" + songs_genre + '\'' +
                ", songs_year=" + songs_year +
                ", album_id=" + album_id +
                //", album=" + album +
                ", songs_type=" + songs_type +
                ", artists=" + artists +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Songs songs = (Songs) o;
        return Objects.equals(songs_id, songs.songs_id) && Objects.equals(songs_name, songs.songs_name) && Objects.equals(songs_genre, songs.songs_genre) && Objects.equals(songs_year, songs.songs_year)  && songs_type == songs.songs_type && Objects.equals(artists, songs.artists);
    }

    public Set<Songs> getAlbumSongs() {
        return albumSongs;
    }

    public void setAlbumSongs(Set<Songs> albumSongs) {
        this.albumSongs = albumSongs;
    }
}
