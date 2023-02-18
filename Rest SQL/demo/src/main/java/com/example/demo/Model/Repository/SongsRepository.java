package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.Songs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongsRepository extends JpaRepository<Songs, Integer>{


}
