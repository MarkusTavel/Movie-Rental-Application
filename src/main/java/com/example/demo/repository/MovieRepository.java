package com.example.demo.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.movie.Movie;

// Mongo DB implementation

public interface MovieRepository extends MongoRepository<Movie, Integer> {
    // Find movie by name
    Movie findByName(String name);
    // Find list of movies given their status of availability
    List<Movie> findByAvailable(boolean available);
}
