package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.Artists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistsRepository extends JpaRepository<Artists, String> {

}
