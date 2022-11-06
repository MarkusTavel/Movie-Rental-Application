package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.MovieData;
import com.example.demo.movie.Movie;
import com.example.demo.repository.MovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

// Service class for housing business logic

@Service
public class MovieService {

    // Formatter for rent price
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Autowired
    private MovieRepository movieRepository;

    // Get all movies from database
    public List<Movie> findAllMovies(){
        return movieRepository.findAll();
    }

    // Get all rented out movies from database
    public List<Movie> findRentedMovies(){
        return movieRepository.findByAvailable(false);
    }

    // Get all available movies from database
    public List<Movie> findAvailableMovies(){
        return movieRepository.findByAvailable(true);
    }

    // Get statistics of movies, most/least popular
    public List<Movie> getStatistics(){
        List<Movie> statistics = movieRepository.findAll();
        // Sort list according to times rented
        statistics.sort(Comparator.comparing(Movie::getTimesRented).reversed());
        return statistics;
    }

    // Find movie by name from database
    public Movie findByName(String name){
        return movieRepository.findByName(name);
    }

    // Add movie to database
    public void addMovie(Movie movie){
        movie.setAvailabe();
        movieRepository.save(movie);
    }

    // Add movie manually to database
    // All parameters must be inserted manually
    public void addMovieManually(String name, LocalDate releaseDate, String director, String actors, String genre, String summary){
        Movie movie = new Movie();
        LocalDate currentDate = LocalDate.now();
        movie.setName(name);
        movie.setReleaseDate(releaseDate);
        movie.setPrice(calculateWeekPrice(releaseDate, currentDate));
        addMovieData(movie, director, actors, genre, summary);
        addMovie(movie);
    }
    
    // Add info to movie
    public void addMovieData(Movie movie, String director, String actors, String genre, String summary){
        MovieData info = new MovieData();
        info.setDirector(director);
        info.setActors(actors);
        info.setGenre(genre);
        info.setSummary(summary);
        movie.setInfo(info);
    }

    // Get movie info
    public MovieData getMovieData(String name){
        return movieRepository.findByName(name).getInfo();
    }

    // Delete all movies from database
    public void deleteAll(){
        movieRepository.deleteAll();
    }

    // Delete movie by name from database
    public void deleteByName(String name){
        movieRepository.delete(findByName(name));
    }

    // Rent out movie
    public String rentOut(String name, int weeks){
        // Find movie from database by name
        Movie movie = movieRepository.findByName(name);
        // If movie is not available return/early exit
        if (!movie.getAvailability()) {
            //System.out.println("The movie is not available");
            return "The movie is not available";
        }
        // Set rent and return dates for movie
        movie.setRentDate(LocalDate.now());
        movie.setReturnDate(LocalDate.now().plusWeeks(weeks));
        // Price valuables for week rent and total rent
        double rentPrice = 0;
        double weekPrice = 0;
        // Calculate price for every week and sum
        for (int i = 1; i <= weeks; i++) {
            // Calculate the price for every specific week 
            // when the movie is rented out and add to rentPrice
            weekPrice = calculateWeekPrice(movie.getReleaseDate(), movie.getRentDate().plusWeeks(i));
            //System.out.println("Week: " + i + " Price: " + weekPrice);
            rentPrice += weekPrice;
        }
        // Round up total rent price
        String totalRentPrice = df.format(rentPrice);
        // Set availability false
        movie.unsetAvailable();
        // Update how many times has been rented out
        movie.setTimesRented(movie.getTimesRented() + 1);
        // Update database/repository
        movieRepository.save(movie);
        //System.out.println(movie.getName());
        //System.out.println("Total rent price: " + totalRentPrice);
        return "Rent out movie - " + movie.getName() + ". For total rent price: " + 
                totalRentPrice + ". Duration: " + movie.getRentDate() +
                " - " + movie.getReturnDate();
    }

    // Return movie from rent
    public void returnMovie(String name){
        // Find movie from database by name
        Movie movie = movieRepository.findByName(name);
        // Remove rent and return dates and set movie to available
        movie.setRentDate(null);
        movie.setReturnDate(null);
        movie.setAvailabe();
        // Update database/repository
        movieRepository.save(movie);
    }

    // Function for calculating week price of movie based on release and current dates
    public double calculateWeekPrice(LocalDate releaseDate, LocalDate currentDate){
        double price = 0;
        // If movie is new set price to 5
        if (releaseDate.isAfter(currentDate.minusWeeks(52))){
            price = 5;
        }
        // If movie is old set price to 1.99
        if (releaseDate.isBefore(currentDate.minusWeeks(156))){
            price = 1.99;
        }
        // If movie is regular set price to 3.49
        if (releaseDate.isBefore(currentDate.minusWeeks(52)) &&
            releaseDate.isAfter(currentDate.minusWeeks(156))) {
            price = 3.49;
        }
        return price;
    }

    // Extra function to read movies from simple json file
    public MovieRepository addMoviesFromFile(String fileName) throws ParseException, IOException{
        // Read movies from file
        byte[] dataFromFile = Files.readAllBytes(Paths.get(fileName));
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
       // Data from file to java object and then JsonNode  
        Object[] movies = objectMapper.readValue(dataFromFile, Object[].class);
        JsonNode node = objectMapper.valueToTree(movies);
        // Dates from Json for converting
        List<JsonNode> dates = node.findValues("releaseDate");
        LocalDate currentDate = LocalDate.now();
        // Add all movies to database and add parameters
        for (int i = 0; i < movies.length; i++) {
            // New movie instance for database
            Movie movie = new Movie();
            // Add name value from Json
            movie.setName(node.get(i).get("name").asText());
            // Read date values from Json and convert to LocalDate
            // https://stackoverflow.com/questions/8746084/string-to-localdate
            String newDate = dates.get(i).asText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate releaseDate = LocalDate.parse(newDate, formatter);
            movie.setReleaseDate(releaseDate);
            // Calculate price based on the release and current date
            movie.setPrice(calculateWeekPrice(releaseDate, currentDate));
            // Add info to movie
            String director = node.get(i).get("info").get("director").asText();
            String actors = node.get(i).get("info").get("actors").asText();
            String genre = node.get(i).get("info").get("genre").asText();
            String summary = node.get(i).get("info").get("summary").asText();  
            addMovieData(movie, director, actors, genre, summary);
            // Add instance to database/repository
            addMovie(movie);
        }
        return movieRepository;
    }

}
