package com.movie.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.movie.Movie;

public class MovieDaoImpl implements MovieDao {

    private SessionFactory sessionFactory;

    // Constructor to initialize the session factory
    public MovieDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveOrUpdateMovie(Movie movie) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(movie);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public Movie getMovieById(Long id) {
        Session session = sessionFactory.openSession();
        Movie movie = session.get(Movie.class, id);
        session.close();
        return movie;
    }

    @Override
    public List<Movie> getAllMovies() {
        Session session = sessionFactory.openSession();
        List<Movie> movies = session.createQuery("FROM Movie", Movie.class).list();
        session.close();
        return movies;
    }

    @Override
    public void deleteMovie(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Movie movie = session.get(Movie.class, id);
            session.delete(movie);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
