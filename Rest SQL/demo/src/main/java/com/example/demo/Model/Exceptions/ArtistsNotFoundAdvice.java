package com.example.demo.Model.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ArtistsNotFoundAdvice{

	@ResponseBody
	@ExceptionHandler(ArtistsNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String songsNotFoundHandler(ArtistsNotFoundException ex) {
		return ex.getMessage();
	}
}
