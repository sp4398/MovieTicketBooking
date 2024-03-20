package com.movie.dao;

import java.util.List;
import com.movie.Movie;

public interface MovieDao {
    // Method to save or update a movie
    void saveOrUpdateMovie(Movie movie);

    // Method to retrieve a movie by its ID
    Movie getMovieById(Long id);

    // Method to retrieve all movies
    List<Movie> getAllMovies();

    // Method to delete a movie by its ID
    void deleteMovie(Long id);
}
