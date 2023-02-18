package com.example.demo.Model.Service;

import com.example.demo.Model.Service.SQLObject;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicReference;


@Service
public class Communication {

	public SQLObject artist = new SQLObject();
	public SQLObject song = new SQLObject();
	private final RestTemplate restTemplate;

	public Communication(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public boolean getArtist(String name){
		artist = new SQLObject();
		String url = "http://localhost:8081/songcollection/artists";
		try {

			JSONParser parser = new JSONParser();
			try {
				JSONObject json = (JSONObject) parser.parse(this.restTemplate.getForObject(url, String.class));
				json = (JSONObject) parser.parse(json.getAsString("_embedded"));
				JSONArray jsonArray = (JSONArray) parser.parse(json.getAsString("artistDTOList"));
				jsonArray.forEach( s->{
					try{
						JSONObject temp = (JSONObject) parser.parse(s.toString());

						if(temp.getAsString("artists_name").equals(name)){
							artist.setName(name);
							artist.setSong_id(temp.getAsString("artists_uuid"));
							artist.setLinks(temp.getAsString("_links"));
						}

					}catch(Exception e){
						System.out.println(e.toString());
					}

				});

			}catch (Exception e){
				System.out.println(e.toString());
			}

			if (artist.getName().isEmpty())
				return false ;
			else
				return true;

		}catch(Exception e){
			return false;
		}

	}
	public boolean getSong(String name) {
		song = new SQLObject();
		// iau toate piesele, daca gasesc piesa iau id ul si numele, apoi caut artistul in artisi, iau uuid si numele
		// caut in join dupa si verific id cu uuid
		String url = "http://localhost:8081/songcollection/songs";

		try {

			JSONParser parser = new JSONParser();
			try {
				JSONObject json = (JSONObject) parser.parse(this.restTemplate.getForObject(url, String.class));
				json = (JSONObject) parser.parse(json.getAsString("_embedded"));
				JSONArray jsonArray = (JSONArray) parser.parse(json.getAsString("songDTOList"));

				var ref = new Object() {
					boolean ok = false;
				};
				jsonArray.forEach( s->{
					try{
					JSONObject temp = (JSONObject) parser.parse(s.toString());

						if(temp.getAsString("songs_name").equals(name) && ref.ok == false){
							song.setName(name);
							song.setSong_id(temp.getAsString("songs_id"));
							song.setLinks(temp.getAsString("_links"));
							ref.ok = true;
						}

					}catch(Exception e){
						System.out.println(e.toString());
					}

				});

			}catch (Exception e){
				System.out.println(e.toString());
			}
			if (song.getName().isEmpty())
				return false ;
			else
				return true;

		}catch(Exception e){
			return false;
		}

	}


	public Integer ifSongIsByArtist() {

		String url = "http://localhost:8081/songcollection/artists/"+artist.id+"/songs";
		System.out.println(url);
		System.out.println(song.id);
		System.out.println(artist.id);
		JSONParser parser = new JSONParser();
		AtomicReference<Integer> ceva = new AtomicReference<>(0);
		ceva.set(0);
		try {
			JSONObject json = (JSONObject) parser.parse(this.restTemplate.getForObject(url, String.class));
			JSONArray jsonArray = (JSONArray) parser.parse(json.getAsString("dtosongs"));
			jsonArray.forEach(s->{
				try{
					JSONObject temp = (JSONObject) parser.parse(s.toString());
					if(temp.getAsString("songs_name").equals(song.getName()) && temp.getAsString("songs_id").equals(song.id)){
						ceva.set(1);
					}

				}catch(Exception e){
					System.out.println(e.toString());
				}
			});

			return ceva.get();
		}catch(Exception e){
			System.out.println(e.toString());
			return 0;
	 	}
	}
}