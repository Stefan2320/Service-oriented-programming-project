package com.example.demo.Model.Service;

public class SQLObject {

	String name;
	public String id;
	public String links;


	public SQLObject(){}

	public SQLObject(String name, String song_id, String links) {
		this.name = name;
		this.id = song_id;
		this.links = links;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSong_id() {
		return id;
	}

	public void setSong_id(String song_id) {
		this.id = song_id;
	}

	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}
}
