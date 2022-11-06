package com.example.demo.movie;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import com.example.demo.data.MovieData;

public class Movie {

    @Id
    private String name;

    private double weekPrice;
    private LocalDate releaseDate;
    private MovieData info;
    private boolean available;
    private LocalDate rentDate;
    private LocalDate returnDate;
    private int timesRented;

    public Movie(){}

    public Movie(String name, double weekPrice, LocalDate releaseDate, MovieData info, boolean available) {
        this.name = name;
        this.weekPrice = weekPrice;
        this.releaseDate = releaseDate;
        this.info = info;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeekPrice() {
        return weekPrice;
    }

    public void setPrice(double weekPrice) {
        this.weekPrice = weekPrice;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDate getRentDate() {
        return rentDate;
    }

    public void setRentDate(LocalDate rentDate) {
        this.rentDate = rentDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public MovieData getInfo() {
        return info;
    }

    public void setInfo(MovieData info) {
        this.info = info;
    }

    public boolean getAvailability() {
        return this.available;
    }

    public void setAvailabe() {
        this.available = true;
    };

    public void unsetAvailable() {
        this.available = false;
    }

    public int getTimesRented() {
        return timesRented;
    }

    public void setTimesRented(int timesRented) {
        this.timesRented = timesRented;
    };

    
}
