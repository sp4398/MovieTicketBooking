package com.movie.dao;

import java.util.List;
import com.movie.Booking;

public interface BookingDao {
    // Method to save or update a booking
    void saveOrUpdateBooking(Booking booking);

    // Method to retrieve a booking by its ID
    Booking getBookingById(Long id);

    // Method to retrieve all bookings
    List<Booking> getAllBookings();

    // Method to delete a booking by its ID
    void deleteBooking(Long id);
}
