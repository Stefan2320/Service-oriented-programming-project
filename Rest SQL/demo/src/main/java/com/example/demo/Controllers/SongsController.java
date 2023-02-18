package com.example.demo.Controllers;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.demo.Model.Entities.Artists;
import com.example.demo.Model.Entities.Songs;
import com.example.demo.Model.Entities.elem_type;
import com.example.demo.Model.Exceptions.SongGenreNotFoundException;
import com.example.demo.Model.Exceptions.SongYearNotAcceptable;
import com.example.demo.Model.Exceptions.SongsAlbumNotFound;
import com.example.demo.Model.Exceptions.SongsNotFoundException;
import com.example.demo.Model.Repository.ArtistsRepository;
import com.example.demo.Model.Repository.SongsRepository;
import com.example.demo.Model.Services.JWTService;
import com.example.demo.Model.Services.SongService;
import com.example.demo.View.SongDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



@RestController
@CrossOrigin
class SongsController {

	@Autowired
	private JWTService jwtService;
	@Autowired
	private SongsRepository repository;
	@Autowired
	private SongService songService;

	@Autowired
	private ArtistsRepository artistRepository;


	@GetMapping("/songcollection/songs")
	@ResponseBody
	ResponseEntity<?> all(@RequestParam(value = "name",required = false)Optional<String> name1,
						  @RequestParam(value = "match",required = false)Optional<String> macth1,
						  @RequestParam(value = "genre",required = false)Optional<String> genre1,
						  @RequestParam(value = "page",required = false)Optional<Integer> page1,
						  @RequestParam(value = "items_per_page",required = false)Optional<Integer> items_per_page1) {
		try {

			String  name = "";
			String match = "";
			String genre = "";
			Integer page = 0;
			Integer items_per_page = 5;

			if(!items_per_page1.isEmpty())
				items_per_page = items_per_page1.get();
			if(!page1.isEmpty())
				page = page1.get();
			if(!genre1.isEmpty())
				genre = genre1.get();
			if(!name1.isEmpty())
			  name = name1.get();
			if(!macth1.isEmpty())
			 match = macth1.get();

			List<Songs> songs = repository.findAll();
			List<SongDTO> DTOsongs = new ArrayList<SongDTO>();
			DTOsongs = songService.convertToListDTO(songs);
			DTOsongs.forEach(song -> {
				song.add(linkTo(methodOn(SongsController.class).one(song.getSongs_id())).withSelfRel());
				song.add(linkTo(methodOn(SongsController.class).all(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty())).withRel("parent"));
			});
			Link songLink = linkTo(methodOn(SongsController.class).all(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty())).withSelfRel();

			if(!name.isEmpty())
				DTOsongs = songService.nameFilterNoMatch(DTOsongs,name);
			if(!name.isEmpty() && match.equals("exact"))
				DTOsongs = songService.nameFilterMatch(DTOsongs,name);
			if(!genre.isEmpty())
				DTOsongs = songService.genreFilter(DTOsongs,genre);
			// Daca se doreste paginare
			if(page != 0) {

				int nr_pages = DTOsongs.size()/items_per_page;
				int unhandled = DTOsongs.size()-nr_pages*items_per_page;
				int next_page = page + 1;
				int prev_page = page - 1;

				//tratez toate cazurile de exemplu: daca am 5 itemi pe pagina si numarul de piese nu e multiplu de 5
				//paginarea la mine e circulara pg1 va avea ca prev ultima pagina si utima pagina va avea ca next prima
				if(page > nr_pages && unhandled == 0){
					Link error = linkTo(methodOn(SongsController.class).all(Optional.empty(),Optional.empty(),Optional.empty() ,Optional.empty(),Optional.empty())).withRel("parent");
					return new ResponseEntity<>("Page was not found!/n"+error,HttpStatus.NOT_FOUND);
				} else if (page > nr_pages +1 ) {
					Link error = linkTo(methodOn(SongsController.class).all(Optional.empty(),Optional.empty(),Optional.empty() ,Optional.empty(),Optional.empty())).withRel("parent");
					return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
				}

				if(next_page > nr_pages && unhandled == 0)
					next_page = 0;
				else if (next_page > nr_pages && page <= nr_pages) {
						next_page = nr_pages+1;
				}else if (next_page > nr_pages && page > nr_pages) {
					next_page = 0;
				}


				if(prev_page < 1 && unhandled == 0)
					prev_page = nr_pages;
				else if (prev_page < 1) {
					prev_page = nr_pages+1;
				}

				if(page > nr_pages)
					items_per_page = unhandled;

				System.out.println(items_per_page);
				DTOsongs = songService.pageFilterDefault(DTOsongs, page,nr_pages,unhandled,items_per_page);


				Optional<Integer> pageNext = Optional.of(next_page);
				Optional<Integer> pagePrev = Optional.of(prev_page);

				Link nextLink = linkTo(methodOn(SongsController.class).all(Optional.empty(),Optional.empty(),Optional.empty(),pageNext ,Optional.empty())).withRel("nextPage");
				Link prevLink = linkTo(methodOn(SongsController.class).all(Optional.empty(),Optional.empty(),Optional.empty(),pagePrev ,Optional.empty())).withRel("prevPage");
				return ResponseEntity.ok(CollectionModel.of(DTOsongs, songLink,nextLink,prevLink));
			}

			return ResponseEntity.ok(CollectionModel.of(DTOsongs, songLink));
		} catch (Exception e){
			return new ResponseEntity<>("Could not find a current representation for the target resource.",HttpStatus.NOT_FOUND);
		}
	}



	@PostMapping("/songcollection/songs")
	ResponseEntity<?> newSong(@RequestBody SongDTO newSong,@RequestHeader (name="Authorization") String jwt1) {

		if(jwtService.getExpire(jwt1) || jwt1.isEmpty()){
			return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
		}


		if(jwtService.JWTcontentManager(jwt1) == Boolean.FALSE && jwtService.JWTartist(jwt1) == Boolean.FALSE){
			return new ResponseEntity<>("No permission!", HttpStatus.FORBIDDEN);
		}



		try {

			Songs song = songService.convertToSongs(newSong);
			if(song.getSongs_type() == elem_type.album && newSong.getAlbum_id()!=null) {
				return new ResponseEntity<>("Albumul nu poate face parte din alt album.", HttpStatus.CONFLICT);
			}

			//Pentru albume
			if(song.getSongs_type() == elem_type.album) {
				if (repository.findById(newSong.getAlbum_id()).isEmpty()) {
					throw new SongsAlbumNotFound(newSong.getAlbum_id());
				} else {
					if (repository.findById(newSong.getAlbum_id()).get().getSongs_type() == elem_type.album) {
						song.setAlbum(repository.findById(newSong.getAlbum_id()).get());
						song.setAlbum_id(newSong.getAlbum_id());
					} else {
						throw new SongsAlbumNotFound(newSong.getAlbum_id(), newSong.getSongs_id(), newSong.getSongs_name());
					}
				}
			}


			if(!(song.getSongs_genre().equals("Pop") || song.getSongs_genre().equals("Rock") || song.getSongs_genre().equals("Hip Hop") || song.getSongs_genre().equals("Jazz")))
				throw new SongGenreNotFoundException(song.getSongs_genre());
			if(song.getSongs_year() < 1800 || song.getSongs_year() > Year.now().getValue())
				throw new SongYearNotAcceptable(song.getSongs_year(),Year.now().getValue());

			repository.save(song);
			SongDTO DTOsong = songService.convertToSongDTO(song);
			DTOsong.add(linkTo(methodOn(SongsController.class).one(DTOsong.getSongs_id())).withSelfRel());
			DTOsong.add(linkTo(methodOn(SongsController.class).all(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty())).withRel("parent"));
			return new ResponseEntity<>(DTOsong, HttpStatus.CREATED);

		}catch(SongGenreNotFoundException e){

			// sau 422 Type sau Bad Request
			return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
		}catch (SongYearNotAcceptable e){

			return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);

		}catch (SongsAlbumNotFound e){

			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}




	@GetMapping("/songcollection/songs/{id}")
	ResponseEntity<?> one(@PathVariable Integer id) {
		try {
			if(repository.findById(id).isEmpty())
				throw new SongsNotFoundException(id);
			Songs song = repository.findById(id).get();
			SongDTO DTOsong = songService.convertToSongDTO(song);

			DTOsong.add(linkTo(methodOn(SongsController.class).one(id)).withSelfRel());
			DTOsong.add(linkTo(methodOn(SongsController.class).all(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty())).withRel("parent"));

			return new ResponseEntity<>(DTOsong, HttpStatus.OK);
		}catch(SongsNotFoundException e){
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}


	@DeleteMapping("/songcollection/songs/{id}")
	ResponseEntity<?> deleteSong(@PathVariable Integer id,@RequestHeader (name="Authorization") String jwt1) {

		if(jwtService.getExpire(jwt1) || jwt1.isEmpty()){
			return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
		}

		if(jwtService.JWTcontentManager(jwt1) == Boolean.FALSE && jwtService.JWTartist(jwt1) == Boolean.FALSE){
			return new ResponseEntity<>("No permission!", HttpStatus.FORBIDDEN);
		}

		try {

			if(repository.findById(id).isEmpty())
				throw new SongsNotFoundException(id);
			Songs song = repository.findById(id).get();
			SongDTO DTOsong = songService.convertToSongDTO(song);


			List<Artists> allArtistList= artistRepository.findAll();
			for (Artists a:allArtistList) {
				if(a.getSongs().contains(song)) {
					Set<Songs> songs = a.getSongs();
					songs.remove(song);
					a.setSongs(songs);
					artistRepository.save(a);
				}
			}

			repository.deleteById(id);
			return new ResponseEntity<>( HttpStatus.NO_CONTENT);
		}catch(SongsNotFoundException e){
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}


}





