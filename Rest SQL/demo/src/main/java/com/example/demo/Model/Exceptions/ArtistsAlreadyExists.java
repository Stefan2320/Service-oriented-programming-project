package com.example.demo.Model.Exceptions;

public class ArtistsAlreadyExists extends RuntimeException{
	ArtistsAlreadyExists(String id){
		super("Artist already exists id: "+id);
	}
}