package com.movie.dao;

import java.util.List;
import com.movie.Theatre;

public interface TheatreDao {
    void saveOrUpdateTheatre(Theatre theatre);

    Theatre getTheatreById(Long id);

    List<Theatre> getAllTheaters();

    void deleteTheater(Long id);
}
