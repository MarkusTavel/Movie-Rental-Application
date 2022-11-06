package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.data.MovieData;
import com.example.demo.movie.Movie;
import com.example.demo.service.MovieService;

@RestController
@RequestMapping(value=("/"), method = {RequestMethod.GET, RequestMethod.POST})
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Get all movies from database
    @GetMapping("/get")
    public List<Movie> displayAllMovies(){
        return movieService.findAllMovies();
    }

    // Get rented movies from database
    @GetMapping("/rented")
    public List<Movie> displayRentedMovies(){
        return movieService.findRentedMovies();
    }

    // Get available movies from database
    @GetMapping("/available")
    public List<Movie> displayAvailableMovies(){
        return movieService.findAvailableMovies();
    }

    // Get movies by popularity
    @GetMapping("/statistics")
    public List<Movie> displayMoviesByPopularity(){
        return movieService.getStatistics();
    }

    // Get movie info
    @GetMapping("/movieInfo/{name}")
    public MovieData getMovieInfo(@PathVariable String name){
        return movieService.getMovieData(name);
    }

    // Add movie to database
    @PutMapping("/addMovie")
    public void addMovie(String name, LocalDate releaseDate, String director, String actors, String genre, String summary){
        // Sample data just for testing the functionality
        // Otherwise an input form on frontend side to get parameters etc
        name = "film 6";
        releaseDate = LocalDate.of(2022, 8, 25);
        director = "some guy";
        actors = "mr actor, mrs actress";
        genre = "mystery";
        summary = "very mysterious movie";
        // For other
        // Add info and movie to database
        movieService.addMovieManually(name, releaseDate, director, actors, genre, summary);
    }

    // Rent movie by name and duration
    @PutMapping("/rent/{name}/{weeks}")
    public ResponseEntity rentMovie(@PathVariable String name, @PathVariable int weeks){
        return ResponseEntity.ok(movieService.rentOut(name, weeks));
    }

    // Return rented movie
    @PutMapping("/return/{name}")
    public void returnMovie(@PathVariable String name){
        movieService.returnMovie(name);
    }

    // Add movies from file
    @PostMapping("/addMovies/{fileName}")
    public void addMoviesFromFile(@PathVariable String fileName) throws ParseException, IOException{
        movieService.addMoviesFromFile(fileName);
    }

    // Delete all movies from database
    @DeleteMapping("/delete")
    public void deleteAll(){
        movieService.deleteAll();
    }

    // Delete movie by name from database
    @DeleteMapping("/delete/{name}")
    public void deleteByName(@PathVariable String name){
        movieService.deleteByName(name);
    }

}
