package com.example.demo.Model.Repository;

import com.example.demo.Model.Entity.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlaylistRepository extends MongoRepository<Playlist, String> {

	public Playlist findByIdUser(String idUser);

}