package com.movie;

import javax.persistence.*;

@Entity
@Table(name = "booking") 
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "movie_id") 
    private Long movieId;
    
    @Column(name = "movieName")
    private String movieName;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "booking_date")
    private String bookingDate;
    
    @Column(name = "number_of_tickets")
    private int numberOfTickets;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showing_id")
	private Showing showing;
    
    // Default constructor
    public Booking() {
    }
    
    // Constructor with parameters
    public Booking(User user, Showing showing) {
        this.user = user;
        this.showing = showing;
    }

    // Getters and setters for all fields
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
    
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", user=" + user.getUsername() +
                ", showing=" + showing.getId() +
                ", bookingDate=" + bookingDate +
                ", numberOfTickets=" + numberOfTickets +
                '}';
    }

}
