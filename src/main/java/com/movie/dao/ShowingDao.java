package com.movie.dao;

import java.util.List;

import com.movie.Showing;

public interface ShowingDao {
    void saveOrUpdateShowing(Showing showing);
    Showing getShowingById(long id);
    void deleteShowing(Showing showing);
    List<Showing> getAllShowings();
}

