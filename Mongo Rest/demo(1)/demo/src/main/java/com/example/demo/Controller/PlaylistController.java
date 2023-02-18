package com.example.demo.Controller;

import com.example.demo.Model.Entity.InnerPlaylist;
import com.example.demo.Model.Entity.Melodies;
import com.example.demo.Model.Entity.Playlist;
import com.example.demo.Model.Repository.PlaylistRepository;
import com.example.demo.Model.Service.Communication;
import com.example.demo.Model.Service.JWTService;
import com.example.demo.Model.Service.PlaylistService;
import com.example.demo.View.PlaylistDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@CrossOrigin
public class PlaylistController {
	private final PlaylistRepository repository;
	@Autowired
	private PlaylistService playlistService;
	@Autowired
	private Communication communicationService;

	@Autowired
	private JWTService jwtService;

	public PlaylistController(PlaylistRepository repository) {
		this.repository = repository;
	}

	//Playlist e ca un fel de cont, unic pentru fiecare utilizator iar innerPlaylist de fapt reprezinta playlistul
	//cu piese

	//Afiseaza toate playlusturile/conturile din Mongo
	@GetMapping("/playlistcollection/playlist")
	ResponseEntity<CollectionModel<PlaylistDTO>> getAllPlaylists() {


		List<Playlist> playlistList =  repository.findAll();

		List<PlaylistDTO> Playlists = playlistService.convertToPlaylistDTOList(playlistList);
		Playlists.forEach(play ->{
			play.add(linkTo(methodOn(PlaylistController.class).getPlaylist(play.getId().toString())).withSelfRel());
			play.add(linkTo(methodOn(PlaylistController.class).getAllPlaylists()).withRel("parent"));
		});

		Link playlistLink = linkTo(methodOn(PlaylistController.class).getAllPlaylists()).withSelfRel();

		return ResponseEntity.ok(CollectionModel.of(Playlists,playlistLink));
	}

	//Vizualizarea playlist/cont pentru un user
	@GetMapping("/playlistcollection/playlist/{id}")
	public ResponseEntity<?> getPlaylist(@PathVariable String id) {
		try {

			Playlist playlist = repository.findByIdUser(id);
			System.out.println(playlist.getId());
			PlaylistDTO DTOplaylist = playlistService.convertToPlaylistDTO(playlist);
			DTOplaylist.add(linkTo(methodOn(PlaylistController.class).getPlaylist(playlist.getId().toString())).withSelfRel());
			DTOplaylist.add(linkTo(methodOn(PlaylistController.class).getAllPlaylists()).withRel("parent"));
			return new ResponseEntity<>(DTOplaylist, HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<>("Playlist id doesn't exist!", HttpStatus.NOT_FOUND);
		}
	}




	//Delete playlist/cont
	@DeleteMapping("/playlistcollection/{id}")
	public ResponseEntity<?> deletePlaylist(@PathVariable String id,@RequestHeader (name="Authorization") String jwt1) {

		try {
			if(jwtService.getExpire(jwt1) || jwt1.isEmpty()){
				return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
			}
			if(repository.findById(id).isEmpty())
				throw new Exception(id);

			Playlist playlist = repository.findById(id).get();
			PlaylistDTO DTOplaylist = playlistService.convertToPlaylistDTO(playlist);


			if(jwtService.JWTclient(jwt1) == TRUE){
				//verifica daca esti owner
				if(!playlist.getIdUser().equals(jwtService.GetUserId(jwt1)))
				return new ResponseEntity<>("No permission!", HttpStatus.FORBIDDEN);
			}else  if(jwtService.JWTadmin(jwt1) == FALSE ){
				return new ResponseEntity<>("No permission!", HttpStatus.FORBIDDEN);
			}
			repository.deleteById(id);
			DTOplaylist.add(linkTo(methodOn(PlaylistController.class).getPlaylist(id)).withSelfRel());
			DTOplaylist.add(linkTo(methodOn(PlaylistController.class).getAllPlaylists()).withRel("collection"));

			return new ResponseEntity<>(DTOplaylist, HttpStatus.NO_CONTENT);
		}catch(Exception e){
			return new ResponseEntity<>("Playlist with id: "+id+" does not exist.",HttpStatus.NOT_FOUND);
		}
	}



	// Faci un "cont", ai voie sa faci un singur cont/playlist account per user (ulterior aici in acest DTO ai sa ai playlisturile for real)

	@RequestMapping(value = "/playlistcollection", method = RequestMethod.POST)
	public ResponseEntity<?> addNewUsers(@RequestBody PlaylistDTO DTOplaylist,@RequestHeader (name="Authorization") String jwt1) {

		try {
			if(jwtService.getExpire(jwt1) || jwt1.isEmpty()){
				return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
			}
			AtomicBoolean ok = new AtomicBoolean(true);
			Playlist playlist = new Playlist();
			String userID =jwtService.GetUserId(jwt1);
			DTOplaylist.setIdUser(userID);
			if(jwtService.JWTclient(jwt1) == FALSE ){
				return new ResponseEntity<>("No permission! This is only for clients.", HttpStatus.FORBIDDEN);
			}
			System.out.println(userID);
			for (Playlist p : repository.findAll()) {
				//exista deja un user cu "playlist" creat
				if (Objects.equals(p.getIdUser(), userID)) {
					return new ResponseEntity<>("You already have a playlist account.", HttpStatus.CONFLICT);
				}
			}
			BeanUtils.copyProperties(DTOplaylist, playlist);
			// Verifica in SQL daca piesa e cantata de artist
			DTOplaylist.getMelodii().forEach(s -> {
				System.out.println(s.artist);
				if (communicationService.getSong(s.song) && communicationService.getArtist(s.artist)) {
						if(communicationService.ifSongIsByArtist() == 1) {
							System.out.println("da");
							communicationService.artist.links = communicationService.artist.links.split("\"href\":\"")[1].replace("\\", "").replace("}}", "").replace("\"", "");
							communicationService.song.links = communicationService.song.links.split("\"href\":\"")[1].replace("\\", "").replace("}}", "").replace("\"", "");
							Link artist_link = Link.valueOf("<" + communicationService.artist.links + ">;rel=\"self\"").withSelfRel();
							Link song_link = Link.valueOf("<" + communicationService.song.links + ">;rel=\"self\"").withSelfRel();
							DTOplaylist.add(artist_link);
							DTOplaylist.add(song_link);
						}else{
							ok.set(false);
							throw new RuntimeException("Song is not by artist");
						}


				}else{
					ok.set(false);
					throw new RuntimeException("Song or Artist are not good");
				}
			});
			repository.save(playlist);
			DTOplaylist.setId(playlist.getId());
			DTOplaylist.add(linkTo(methodOn(PlaylistController.class).getPlaylist(playlist.getId().toString())).withSelfRel());
			DTOplaylist.add(linkTo(methodOn(PlaylistController.class).getAllPlaylists()).withRel("parent"));
			return new ResponseEntity<>(DTOplaylist, HttpStatus.CREATED);
		}catch(Exception e){

			return new ResponseEntity<>(e.toString(), HttpStatus.CONFLICT);
		}
	}


	//Adauga un innerPlaylist/ playlist la un cont/playlist
	@RequestMapping(value = "/playlistcollection/{id2}/playlist", method = RequestMethod.PUT)
	public ResponseEntity<?> createNewPlaylist(@PathVariable String id2, @RequestBody InnerPlaylist innerPlaylist, @RequestHeader (name="Authorization") String jwt1){
		AtomicReference<HttpStatus> status = new AtomicReference<>(HttpStatus.CONFLICT);
		try{
			if(jwtService.getExpire(jwt1) || jwt1.isEmpty()){
				return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
			}


			Boolean userExists = FALSE;
			Playlist playlist = new Playlist();
			String id = jwtService.GetUserId(jwt1);
			for (Playlist p : repository.findAll()) {
				if (Objects.equals(p.getIdUser(), id)) {
					BeanUtils.copyProperties(p,playlist);
					userExists = TRUE;
				}
			}
			if(jwtService.JWTclient(jwt1) == FALSE || !id.equals(id2)){
				return new ResponseEntity<>("No permission! ", HttpStatus.FORBIDDEN);
			}

			if(userExists == FALSE) {
				status.set(HttpStatus.NOT_FOUND);
				throw new Exception("User id was not found.");
			}


			ArrayList<InnerPlaylist> innerPlaylistUser = playlist.getPlaylists();
			innerPlaylistUser.forEach( s->{
				if(s.getName().equals(innerPlaylist.getName())){
					status.set(HttpStatus.CONFLICT);
					throw new RuntimeException("Name already exists.");
				}
			});

			innerPlaylist.getMelodii().forEach(s->{
				AtomicBoolean ok = new AtomicBoolean(true);
				if (communicationService.getSong(s.song) && communicationService.getArtist(s.artist)) {
					if (communicationService.ifSongIsByArtist() == 1) {

						communicationService.artist.links = communicationService.artist.links.split("\"href\":\"")[1].replace("\\", "").replace("}}", "").replace("\"", "");
						communicationService.song.links = communicationService.song.links.split("\"href\":\"")[1].replace("\\", "").replace("}}", "").replace("\"", "");
						s.setSongLink(String.valueOf(communicationService.song.links));
						s.setArtistLink(String.valueOf(communicationService.artist.links));
					} else {
						ok.set(false);
						status.set(HttpStatus.CONFLICT);
						throw new RuntimeException("Song is not by artist");
					}

				} else {
					ok.set(false);
					status.set(HttpStatus.CONFLICT);
					throw new RuntimeException("Song or Artist are not good");
				}
			});

			playlist.addToPlaylist(innerPlaylist);
			repository.save(playlist);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}catch(Exception e) {

			return new ResponseEntity<>(e.toString(),status.get());
		}

	}

	//Adauga la innerPlaylist o melodie sau sterge o melodie
	@RequestMapping(value = "/playlistcollection/{id2}/playlists/{name}", method = RequestMethod.PUT)
	public ResponseEntity<?> addSongToPlaylist(@RequestBody Melodies piesa, @RequestParam(value = "option",required = false) Optional<String> option, @PathVariable String name, @PathVariable String id2, @RequestHeader (name="Authorization") String jwt1){


	if(option.get().equals("add")) {
		AtomicReference<HttpStatus> status = new AtomicReference<>(HttpStatus.OK);
		try {
			if (jwtService.getExpire(jwt1) || jwt1.isEmpty()) {
				return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
			}

			String id = jwtService.GetUserId(jwt1);
			Boolean userExists = FALSE;
			Boolean playlistExists = TRUE;
			Playlist playlist = new Playlist();
			if (jwtService.JWTclient(jwt1) == FALSE || !id2.equals(id)) {
				return new ResponseEntity<>("No permission!", HttpStatus.FORBIDDEN);
			}
			for (Playlist p : repository.findAll()) {
				if (Objects.equals(p.getIdUser(), id)) {
					BeanUtils.copyProperties(p, playlist);
					userExists = TRUE;
				}
			}

			if (userExists == FALSE) {
				status.set(HttpStatus.NOT_FOUND);
				throw new Exception("User id was not found.");
			}

			if (playlist.getInnerplaylist(name).existsSong(piesa.getSong()) == TRUE) {
				return new ResponseEntity<>("Song found already in innerplaylist!", HttpStatus.CONFLICT);
			}

			AtomicBoolean ok = new AtomicBoolean(true);

			if (!piesa.artist.contains("empty")) {
				if (communicationService.getSong(piesa.song) && communicationService.getArtist(piesa.artist)) {
					if (communicationService.ifSongIsByArtist() == 1) {

						communicationService.artist.links = communicationService.artist.links.split("\"href\":\"")[1].replace("\\", "").replace("}}", "").replace("\"", "");
						communicationService.song.links = communicationService.song.links.split("\"href\":\"")[1].replace("\\", "").replace("}}", "").replace("\"", "");
						piesa.setSongLink(String.valueOf(communicationService.song.links));
						piesa.setArtistLink(String.valueOf(communicationService.artist.links));
					} else {
						ok.set(false);
						throw new RuntimeException("Song is not by artist");
					}

				} else {
					ok.set(false);
					throw new RuntimeException("Song or Artist are not good");
				}
			} else {

				communicationService.getSong(piesa.song);
				communicationService.song.links = communicationService.song.links.split("\"href\":\"")[1].replace("\\", "").replace("}}", "").replace("\"", "");
				piesa.setSongLink(String.valueOf(communicationService.song.links));

			}

			playlist.addToInnerplaylist(name, piesa);

			if (playlist.getInnerplaylist(name).getName() == null)
				return new ResponseEntity<>("Playlist with name: " + name + " doesn't exists", HttpStatus.CONFLICT);

			repository.save(playlist);

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(e.toString(), HttpStatus.CONFLICT);
		}
	} else if (option.get().equals("rem")) {
		String id = jwtService.GetUserId(jwt1);
		try {

			if(jwtService.getExpire(jwt1) || jwt1.isEmpty()){
				return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
			}

			if(jwtService.JWTclient(jwt1) == FALSE ){
				return new ResponseEntity<>("No permission!", HttpStatus.FORBIDDEN);
			}

			//String id = jwtService.GetUserId(jwt1);
			Boolean userExists = FALSE;
			Playlist playlist = new Playlist();

			for (Playlist p : repository.findAll()) {
				if (Objects.equals(p.getIdUser(), id)) {
					BeanUtils.copyProperties(p,playlist);
					userExists = TRUE;
				}
			}


			if(userExists == FALSE) {
				return new ResponseEntity<>("User id was not found.", HttpStatus.NOT_FOUND);
			}

			if(playlist.getInnerplaylist(name) == null)
				return new ResponseEntity<>("Innerplaylist not found!", HttpStatus.NOT_FOUND);

			InnerPlaylist ip = playlist.getInnerplaylist(name);
			playlist.deleteInnerPlaylist(ip);
			repository.save(playlist);

			return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
		}catch(Exception e){
			return new ResponseEntity<>("Playlist with id: "+id+" does not exist.",HttpStatus.NOT_FOUND);
		}
	}else{
		return new ResponseEntity<>("e.toString()", HttpStatus.CONFLICT);
	}


	}



	@DeleteMapping("/playlistcollection/{name}/playlist/{song}")
	public ResponseEntity<?> deleteInnerPlaylistSong(@PathVariable String name,@PathVariable String song,@RequestHeader (name="Authorization") String jwt1) {
		String id = jwtService.GetUserId(jwt1);
		try {

			if(jwtService.getExpire(jwt1) || jwt1.isEmpty()){
				return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
			}
			if(jwtService.JWTclient(jwt1) == FALSE ){
				return new ResponseEntity<>("No permission!", HttpStatus.FORBIDDEN);
			}


			try{

				repository.findByIdUser(id);

			}catch(Exception e){
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			Playlist playlist2 = repository.findByIdUser(id);

			//resursa interioara
			if(playlist2.getInnerplaylist(name) == null)
				return new ResponseEntity<>("Innerplaylist not found!", HttpStatus.CONFLICT);

			if(playlist2.getInnerplaylist(name).existsSong(song) == FALSE){
				return new ResponseEntity<>("Song not found in innerplaylist!", HttpStatus.CONFLICT);
			}

			playlist2.getInnerplaylist(name).deleteSong(song);
			repository.save(playlist2);
			return new ResponseEntity<>( HttpStatus.NO_CONTENT);
		}catch(Exception e){
			return new ResponseEntity<>("Playlist does not exist.",HttpStatus.NOT_FOUND);
		}
	}
}


