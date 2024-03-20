package com.movie;

import javax.persistence.*;

@Entity
@Table(name = "movie") // Specify the name of the database table
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "genres")
    private String genres;
    
    @Column(name = "director")
    private String director;
    
    @Column(name = "year")
    private int year;
    
    // Default constructor
    public Movie() {
    }
    
    // Constructor with parameters
    public Movie(String title, String genres, String director, int year) {
        this.title = title;
        this.genres = genres;
        this.director = director;
        this.year = year;
    }

    // Getters and setters for all fields
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genres='" + genres + '\'' +
                ", director='" + director + '\'' +
                ", year=" + year +
                '}';
    }
}
