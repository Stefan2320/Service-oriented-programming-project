package com.example.demo.Model.Exceptions;

public class SongYearNotAcceptable extends RuntimeException{
	public SongYearNotAcceptable(Integer year,Integer current) {
		super(year+" year is invalid, values accapted are : 1800-"+ current + "(including)." );
	}
}
