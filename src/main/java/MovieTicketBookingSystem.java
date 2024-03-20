import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import com.movie.Booking;

@Entity
public class MovieTicketBookingSystem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public MovieTicketBookingSystem() {
        this.bookings = new ArrayList<>();
    }

    public void bookTicket(Booking booking) {
        bookings.add(booking);
        System.out.println("Ticket booked successfully!");
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }
}
