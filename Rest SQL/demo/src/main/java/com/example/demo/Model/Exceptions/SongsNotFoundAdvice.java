package com.example.demo.Model.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class SongsNotFoundAdvice{

	@ResponseBody
	@ExceptionHandler(SongsNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String songsNotFoundHandler(SongsNotFoundException ex) {
		return ex.getMessage();
	}
}