package com.example.demo.Model.Exceptions;

public class ArtistsNotFoundException extends RuntimeException{
	public ArtistsNotFoundException(String id){
		super("Could not find artist with the following id: "+id);
	}
}
