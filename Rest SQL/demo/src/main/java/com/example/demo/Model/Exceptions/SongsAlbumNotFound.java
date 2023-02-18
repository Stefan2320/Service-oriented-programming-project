package com.example.demo.Model.Exceptions;

public class SongsAlbumNotFound extends RuntimeException{

	public SongsAlbumNotFound(Integer message) {
		super("There is no album with album_id: " + message+".");
	}

	public SongsAlbumNotFound(Integer message,Integer id,String name) {
		super("There is no album with album_id: " + message+".");
	}
}
