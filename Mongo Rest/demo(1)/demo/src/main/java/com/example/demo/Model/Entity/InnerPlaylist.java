package com.example.demo.Model.Entity;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class InnerPlaylist {

	private String name;
	private ArrayList<Melodies> melodii = new ArrayList<Melodies>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Melodies> getMelodii() {
		return melodii;
	}

	public void setMelodii(ArrayList<Melodies> melodii) {
		this.melodii = melodii;
	}

	public void addMelodie(Melodies melodie){this.melodii.add(melodie);}

	public void deleteSong(String name){
		int index = -1;
		for( int i = 0 ; i < melodii.size();i++)
			if(melodii.get(i).getSong().equals(name))
				index = i;

		if(index != -1) {
			System.out.println("adadas");
			melodii.remove(index);
		}
		else
			System.out.println("EROARE");
	}

	public boolean existsSong(String name){
		System.out.println("asdafaaa");
		System.out.println(name);
		for( int i = 0 ; i < melodii.size();i++)
			if(melodii.get(i).song.equals(name)) {
				System.out.println(melodii.get(i).song);
				return TRUE;
			}
		System.out.println("asdafaaa");
		return FALSE;

	}
}
