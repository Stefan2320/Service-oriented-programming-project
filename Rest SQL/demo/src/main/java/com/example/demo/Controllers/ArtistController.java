package com.example.demo.Controllers;


import com.example.demo.Model.Entities.Artists;
import com.example.demo.Model.Entities.Songs;
import com.example.demo.Model.Exceptions.ArtistsNotFoundException;
import com.example.demo.Model.Repository.ArtistsRepository;
import com.example.demo.Model.Repository.SongsRepository;
import com.example.demo.Model.Services.ArtistService;
import com.example.demo.Model.Services.JWTService;
import com.example.demo.Model.Services.SongService;
import com.example.demo.View.ArtistDTO;
import com.example.demo.View.SongArtistDTO;
import com.example.demo.View.SongDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;




@RestController
@CrossOrigin
public class ArtistController {

	@Autowired
	private final ArtistsRepository repository;


	@Autowired
	private JWTService jwtService;
	@Autowired
	private SongsRepository songRepository;
	@Autowired
	private SongService songService;

	@Autowired
	private ArtistService artistService;

	ArtistController(ArtistsRepository repository) {
		this.repository = repository;
	}


	@GetMapping("/songcollection/artists")
	ResponseEntity<?> all(@RequestParam(value = "name",required = false) Optional<String> name1,
												   @RequestParam(value = "part",required = false) Optional<String> partial1) {

		String partial ="";
		String  name = "";

		if(!partial1.isEmpty())
			partial = partial1.get();
		if(!name1.isEmpty())
			name = name1.get();

		List<Artists> artist= repository.findAll();
		List<ArtistDTO> DTOartist = new ArrayList<ArtistDTO>();
		DTOartist= artistService.convertToListDTO(artist);
		DTOartist.forEach(artists ->{
			artists.add(linkTo(methodOn(ArtistController.class).one(artists.getArtists_uuid())).withSelfRel());
			artists.add(linkTo(methodOn(ArtistController.class).all(Optional.empty(),Optional.empty())).withRel("parent"));
		});
		// verific nume complet
		if(!name.isEmpty()) {
				DTOartist = artistService.nameFilter(DTOartist, name);
				if(DTOartist.size() == 0){
					Link error = linkTo(methodOn(ArtistController.class).all(Optional.empty(),Optional.empty())).withRel("parent");
					return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
				}

		}

		// verific nume partial
		if(!partial.isEmpty()) {
			for( int i  = 0 ; i < DTOartist.size();i++){
				ArtistDTO a = new ArtistDTO("",DTOartist.get(i).getArtists_name(),"");
				DTOartist.set(i,a);
			}

			if(DTOartist.size() == 0){
				Link error = linkTo(methodOn(ArtistController.class).all(Optional.empty(),Optional.empty())).withRel("parent");
				return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
			}

		}



		Link artistLink = linkTo(methodOn(ArtistController.class).all(Optional.empty(),Optional.empty())).withSelfRel();
		return  ResponseEntity.ok(CollectionModel.of(DTOartist,artistLink));
	}


	//Adauga un artist nou / modifica un artist existent
 	@PutMapping("/songcollection/artists/{uuid}")
	ResponseEntity<?> newArtist(@RequestBody Artists newArtist, @PathVariable String uuid,@RequestHeader (name="Authorization") String jwt1) {
		newArtist.setArtists_uuid(uuid);
		HttpStatus status;
		Boolean permission = jwtService.JWTcontentManager(jwt1);
		if(jwtService.getExpire(jwt1) || jwt1.isEmpty()){
			return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
		}

		//Nu exista inainte
		ArtistDTO DTOArtist = artistService.convertToArtistDTO(newArtist);
		if(repository.findById(uuid).isEmpty() && permission == Boolean.TRUE) {
			status = HttpStatus.CREATED;
			repository.save(newArtist);

		}//Se modifica doar o parte, el exista deja
		else if(permission == Boolean.TRUE){
			DTOArtist = artistService.convertToArtistDTO(repository.findById(uuid).get());
			Set<Songs> songs = repository.findById(uuid).get().getSongs();
			newArtist.setSongs(songs);
			repository.save(newArtist);
			status = HttpStatus.NO_CONTENT;

		}else{
			status = HttpStatus.FORBIDDEN;
		}

		DTOArtist.add(linkTo(methodOn(ArtistController.class).one(newArtist.getArtists_uuid())).withSelfRel());
		DTOArtist.add(linkTo(methodOn(ArtistController.class).all(Optional.empty(),Optional.empty())).withRel("parent"));

		return new ResponseEntity<>(DTOArtist, status);
	}


	//Vizualizare artist
	@GetMapping("/songcollection/artists/{id}")
	ResponseEntity<?> one(@PathVariable String id) {
		try{
			repository.findById(id).get();
		}catch (Exception e){
			return  new ResponseEntity<>("Artist not found",HttpStatus.NOT_FOUND);
		}
		Artists artists = repository.findById(id)
				.orElseThrow(() -> new ArtistsNotFoundException(id));
		ArtistDTO  artistDTO= artistService.convertToArtistDTO(artists);
		artistDTO.add(linkTo(methodOn(ArtistController.class).one(id)).withSelfRel());
		artistDTO.add(linkTo(methodOn(ArtistController.class).all(Optional.empty(),Optional.empty())).withRel("parent"));
		return new ResponseEntity<>(artistDTO, HttpStatus.OK);
	}

	
	//Vezi toate piesele unui artist
	@GetMapping("/songcollection/artists/{uuid}/songs")
	ResponseEntity<?> getSongsByArtist(@PathVariable String uuid){
		try {

			if(repository.findById(uuid).isEmpty())
				throw new ArtistsNotFoundException(uuid);

			Artists find = repository.findById(uuid).get();

			ArtistDTO DTOartists = artistService.convertToArtistDTO(find);
			List<Songs> song = new ArrayList<>(find.songs);
			List<SongDTO> songs = songService.convertToListDTO(song);
			songs.forEach(songa -> {
				songa.add(linkTo(methodOn(SongsController.class).one(songa.getSongs_id())).withSelfRel().withType("GET"));
				songa.add(linkTo(methodOn(ArtistController.class).one(DTOartists.getArtists_uuid())).withRel("parent"));
			});

			DTOartists.setDTOsongs(songs);
			DTOartists.add(linkTo(methodOn(ArtistController.class).one(DTOartists.getArtists_uuid())).withSelfRel());
			return new ResponseEntity<>(DTOartists, HttpStatus.OK);
		}catch (ArtistsNotFoundException e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	// Sterge un artist
	@DeleteMapping("/songcollection/artists/{uuid}")
	ResponseEntity<?> deleteArtist(@PathVariable String uuid,@RequestHeader (name="Authorization") String jwt1) {
		if(jwtService.getExpire(jwt1) || jwt1.isEmpty()){
			return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
		}
		HttpStatus x = HttpStatus.NO_CONTENT;
		try{repository.findById(uuid).get();}catch (Exception e){
			Link error = linkTo(methodOn(ArtistController.class).all(Optional.empty(),Optional.empty())).withRel("parent");
			return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
		}

		Artists artist = repository.findById(uuid).get();
		artist.setSongs(null);
		ArtistDTO DTOartist = artistService.convertToArtistDTO(artist);
		if(!jwtService.JWTcontentManager(jwt1)) {
			x = HttpStatus.FORBIDDEN;
		}else{
			repository.deleteById(uuid);
		}

		return new ResponseEntity<>(x);

	}



	// Sterge o piesa  cantata de un artist, sterg din join, chiar si daca ea inlocuieste o resursa  rfc-ul spune pentru delete:
	// remove the association between the target resource and its current functionality, la mine asta fiind ca desprinde
	// artistul de piesa.
	@DeleteMapping("/songcollection/artists/{uuid}/songs/{id}")
	ResponseEntity<?> deleteSong(@PathVariable String uuid,@PathVariable int id,@RequestHeader (name="Authorization") String jwt1) {


		HttpStatus x = HttpStatus.NO_CONTENT;
		try{repository.findById(uuid).get();}catch (Exception e){return  new ResponseEntity<>("Artist not found!", HttpStatus.NOT_FOUND);}
		Artists artist = repository.findById(uuid).get();
		ArtistDTO DTOartist = artistService.convertToArtistDTO(artist);
		if(jwtService.getExpire(jwt1) || jwt1.isEmpty()){
			return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
		}
		if(!jwtService.JWTcontentManager(jwt1)) {
			x = HttpStatus.FORBIDDEN;
		}else{

			Set<Songs> music = artist.getSongs();
			try{
				Songs rem = songRepository.findById(id).get();
				music.remove(rem);
				artist.setSongs(music);
				repository.save(artist);
			}catch (Exception e){
				x = HttpStatus.NOT_FOUND;
				return  new ResponseEntity<>("Song was not found!",x);
			}

		}
		DTOartist.add(linkTo(methodOn(ArtistController.class).one(uuid)).withSelfRel());						// care a fost "comanda"
		DTOartist.add(linkTo(methodOn(ArtistController.class).all(Optional.empty(),Optional.empty())).withRel("collection"));				// cum se acceseaza toti copii
		return new ResponseEntity<>(DTOartist, x);

	}
}
