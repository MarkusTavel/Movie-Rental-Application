package com.example.demo.data;

public class MovieData {
    
    // Simple data class to store info
    // Actors, producers, directors, summary, genre etc..

    private String director;
    private String actors;
    private String summary;
    private String genre;

    public MovieData(String director, String actors, String summary, String genre) {
        this.director = director;
        this.actors = actors;
        this.summary = summary;
        this.genre = genre;
    }

    public MovieData(){}

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String string) {
        this.actors = string;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
    
}
