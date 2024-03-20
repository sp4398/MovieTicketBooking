package com.movie;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "showings")
public class Showing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "show_time")
    private Date showTime;

    @Column(name = "available_seats")
    private int availableSeats;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public long getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	
	public Showing() {
    }

	public Showing(long id, Movie movie, Date showTime, int availableSeats) {
		super();
		this.movie = movie;
		this.showTime = showTime;
		this.availableSeats = availableSeats;
	}

}
