package com.movie.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.movie.Showing;

public class ShowingDaoImpl implements ShowingDao {

    private final SessionFactory sessionFactory;

    public ShowingDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveOrUpdateShowing(Showing showing) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(showing);
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
    public Showing getShowingById(long id) {
        Session session = sessionFactory.openSession();
        Showing showing = session.get(Showing.class, id);
        session.close();
        return showing;
    }
    @Override
    public void deleteShowing(Showing showing) { 
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(showing);
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
    public List<Showing> getAllShowings() {
        Session session = sessionFactory.openSession();
        List<Showing> showings = session.createQuery("FROM Showing", Showing.class).list();
        session.close();
        return showings;
    }

}

