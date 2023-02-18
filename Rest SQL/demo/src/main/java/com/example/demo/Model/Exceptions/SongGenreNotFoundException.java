package com.example.demo.Model.Exceptions;

public class SongGenreNotFoundException extends RuntimeException{
	public SongGenreNotFoundException(String genre) {
		super("The "+genre + " genre doesn't exist try something from: Pop,Rock, Hip Hop, Jazz.");
	}
}
