package com.movie.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import com.movie.Booking;

public class BookingDaoImpl implements BookingDao {
    private final SessionFactory sessionFactory;

    public BookingDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveOrUpdateBooking(Booking booking) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(booking);
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
    public Booking getBookingById(Long id) {
        Session session = sessionFactory.openSession();
        Booking booking = session.get(Booking.class, id);
        session.close();
        return booking;
    }

    @Override
    public List<Booking> getAllBookings() {
        Session session = sessionFactory.openSession();
        List<Booking> bookings = session.createQuery("FROM Booking", Booking.class).list();
        session.close();
        return bookings;
    }

    @Override
    public void deleteBooking(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Booking booking = session.get(Booking.class, id);
            session.delete(booking);
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
