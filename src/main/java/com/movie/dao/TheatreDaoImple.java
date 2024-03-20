package com.movie.dao;

import com.movie.Theatre;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class TheatreDaoImple implements TheatreDao {
    private final SessionFactory sessionFactory;

    public TheatreDaoImple(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveOrUpdateTheatre(Theatre theatre) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(theatre);
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
    public Theatre getTheatreById(Long id) {
        Session session = sessionFactory.openSession();
        Theatre theatre = session.get(Theatre.class, id);
        session.close();
        return theatre;
    }

    @Override
    public List<Theatre> getAllTheaters() {
        Session session = sessionFactory.openSession();
        List<Theatre> theaters = null;
        try {
            theaters = session.createQuery("FROM Theatre", Theatre.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return theaters;
    }

    @Override
    public void deleteTheater(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Theatre theatre = session.get(Theatre.class, id);
            session.delete(theatre);
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
