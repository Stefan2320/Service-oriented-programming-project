package com.example.demo.Model.Exceptions;

public class SongsNotFoundException extends RuntimeException{
	public SongsNotFoundException(Integer id){
		super("Could not find song with the following id: "+id);
	}
}
