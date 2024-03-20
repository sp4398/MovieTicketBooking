import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.movie.*;
import com.movie.dao.ShowingDao;
import com.movie.dao.ShowingDaoImpl;
import com.movie.dao.UserDao;
import com.movie.dao.UserDaoImpl;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		MovieTicketBookingSystem bookingSystem = new MovieTicketBookingSystem();
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = configuration.buildSessionFactory();

		UserDao userDao = new UserDaoImpl(sessionFactory);

		while (true) {
			System.out.println("Welcome to the Movie Ticket Booking System");
			System.out.println("1. Sign up");
			System.out.println("2. Sign in");
			System.out.println("3. Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				signUp(scanner, userDao);
				break;
			case 2:
				User user = signIn(scanner, userDao, bookingSystem, sessionFactory);
				if (user != null) {
					if (user.getRole() == 1) {
						adminActions(scanner, userDao, sessionFactory);
					} else {
						regularUserActions(scanner, bookingSystem, sessionFactory);
					}
				}
				break;
			case 3:
				System.out.println("Thank you for using the Movie Ticket Booking System. Goodbye!");
				System.exit(0);
			default:
				System.out.println("Invalid choice. Please enter a valid option.");
			}
		}
	}

	private static void regularUserActions(Scanner scanner, MovieTicketBookingSystem bookingSystem,
			SessionFactory sessionFactory) {
		System.out.println("Regular user actions:");
		System.out.println("1. View movies");
		System.out.println("2. Book a ticket");
		System.out.println("3. View bookings");
		System.out.println("4. Exit");
		System.out.print("Enter your choice: ");

		int choice = scanner.nextInt();
		scanner.nextLine();

		switch (choice) {
		case 1:
			viewMovies(sessionFactory);
			break;
		case 2:
			bookTicket(scanner, sessionFactory);
			break;
		case 3:
			viewBookings(scanner, sessionFactory);
			break;
		case 4:
			System.out.println("Exiting regular user panel.");
			break;
		default:
			System.out.println("Invalid choice. Please enter a valid option.");
		}

	}

	private static void viewBookings(Scanner scanner, SessionFactory sessionFactory) {
	    Session session = sessionFactory.openSession();
	    Transaction transaction = null;

	    try {
	        transaction = session.beginTransaction();
	        User currentUser = session.get(User.class, 1L);
	        List<Booking> bookings = session.createQuery(
	                "SELECT b FROM Booking b JOIN FETCH b.user WHERE b.user.id = :userId",
	                Booking.class
	            )
	            .setParameter("userId", currentUser.getId())
	            .getResultList();

	        if (bookings.isEmpty()) {
	            System.out.println("No bookings found.");
	        } else {
	            System.out.println("Your Bookings:");
	            for (Booking booking : bookings) {
	                System.out.println(booking);
	            }
	        }

	        transaction.commit();
	    } catch (HibernateException e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }
	}


	private static void bookTicket(Scanner scanner, SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			System.out.println("Available Movies:");
			List<Movie> availableMovies = session.createQuery("FROM Movie", Movie.class).list();
			if (availableMovies.isEmpty()) {
				System.out.println("No movies available for booking.");
				return;
			}
			for (Movie movie : availableMovies) {
				System.out.println("ID: " + movie.getId() + ", Title: " + movie.getTitle());
			}

			System.out.print("Enter the ID of the movie you want to book a ticket for: ");
			long movieId = scanner.nextLong();
			scanner.nextLine();

			System.out.println("Available Showings for the selected movie:");
			List<Showing> availableShowings = session
					.createQuery("FROM Showing WHERE movie_id = :movieId", Showing.class)
					.setParameter("movieId", movieId).list();
			if (availableShowings.isEmpty()) {
				System.out.println("No showings available for the selected movie.");
				return;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Showing showing : availableShowings) {
				System.out.println("ID: " + showing.getId() + ", Time: " + sdf.format(showing.getShowTime()));
			}

			System.out.print("Enter the ID of the showing you want to book a ticket for: ");
			long showingId = scanner.nextLong();
			scanner.nextLine();

			Showing selectedShowing = session.get(Showing.class, showingId);
			if (selectedShowing.getAvailableSeats() <= 0) {
				System.out.println("No available seats for the selected showing.");
				return;
			}

			selectedShowing.setAvailableSeats((int) (selectedShowing.getAvailableSeats() - 1));

			User user = session.get(User.class, 1L);
			Booking booking = new Booking(user, selectedShowing);
			session.save(booking);

			transaction.commit();
			System.out.println("Ticket booked successfully.");
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private static void viewMovies(SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			List<Movie> movies = session.createQuery("FROM Movie", Movie.class).list();

			if (movies.isEmpty()) {
				System.out.println("No movies available.");
			} else {
				System.out.println("Movies:");
				for (Movie movie : movies) {
					System.out.println(movie.getTitle());
				}
			}

			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private static void signUp(Scanner scanner, UserDao userDao) {
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();
		System.out.print("Enter email: ");
		String email = scanner.nextLine();
		System.out.print("Enter name: ");
		String name = scanner.nextLine();

		User newUser = new User(username, email, password, name, 2);
		userDao.saveOrUpdateUser(newUser);

		System.out.println("Sign up successful.");

	}

	private static User signIn(Scanner scanner, UserDao userDao, MovieTicketBookingSystem bookingSystem,
			SessionFactory sessionFactory) {
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();

		User user = userDao.authenticateUser(username, password);
		if (user != null) {
			System.out.println("Sign in successful.");

			int role = user.getRole();

			if (role == 1) {
				adminActions(scanner, userDao, sessionFactory);
			} else {
				regularUserActions(scanner, bookingSystem, sessionFactory);
			}
		} else {
			System.out.println("Invalid username or password. Please try again.");
		}
		return user;
	}

	private static void adminActions(Scanner scanner, UserDao userDao, SessionFactory sessionFactory) {
		System.out.println("Admin actions:");
		System.out.println("1. Add Movie");
		System.out.println("2. Add Theatre");
		System.out.println("3. Add Showing");
		System.out.println("4. Edit Movie");
		System.out.println("5. Edit Theatre");
		System.out.println("6. Edit Showing");
		System.out.println("7. Delete Movie");
		System.out.println("8. Delete Theatre");
		System.out.println("9. Delete Showing");
		System.out.println("10. Exit");
		System.out.print("Enter your choice: ");

		int choice = scanner.nextInt();
		scanner.nextLine();

		switch (choice) {
		case 1:
			addMovie(scanner, sessionFactory);
			break;
		case 2:
			addTheatre(scanner, sessionFactory);
			break;
		case 3:
			addShowing(scanner, sessionFactory);
			break;
		case 4:
			editMovie(scanner, sessionFactory);
			break;
		case 5:
			editTheatre(scanner, sessionFactory);
			break;
		case 6:
			editShowing(scanner, sessionFactory);
			break;
		case 7:
			deleteMovie(scanner, sessionFactory);
			break;
		case 8:
			deleteTheatre(scanner, sessionFactory);
			break;
		case 9:
			deleteShowing(scanner, sessionFactory);
			break;
		case 10:
			System.out.println("Exiting admin panel.");
			break;
		default:
			System.out.println("Invalid choice. Please enter a valid option.");
		}
	}

	private static void deleteShowing(Scanner scanner, SessionFactory sessionFactory) {
		System.out.println("Deleting a Showing");

		System.out.print("Enter showing ID to delete: ");
		long showingId = scanner.nextLong();
		scanner.nextLine();

		ShowingDao showingDao = new ShowingDaoImpl(sessionFactory);
		Showing showing = showingDao.getShowingById(showingId);

		if (showing != null) {
			showingDao.deleteShowing(showing);

			System.out.println("Showing deleted successfully.");
		} else {
			System.out.println("Showing with ID " + showingId + " not found.");
		}
	}

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static void editShowing(Scanner scanner, SessionFactory sessionFactory) {
	    System.out.println("Editing a Showing");

	    System.out.print("Enter showing ID to edit: ");
	    long showingId = scanner.nextLong();
	    scanner.nextLine(); 

	    ShowingDao showingDao = new ShowingDaoImpl(sessionFactory);
	    Showing showing = showingDao.getShowingById(showingId);

	    if (showing != null) {
	        System.out.print("Enter new show time (yyyy-MM-dd HH:mm:ss): ");
	        String showTimeStr = scanner.nextLine();
	        Date newShowTime = null;
	        try {
	            newShowTime = DATE_FORMAT.parse(showTimeStr);
	        } catch (ParseException e) {
	            System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm:ss format.");
	            return;
	        }

	        System.out.print("Enter new available seats: ");
	        int newAvailableSeats = scanner.nextInt();
	        scanner.nextLine();

	        showing.setShowTime(newShowTime);
	        showing.setAvailableSeats(newAvailableSeats);

	        showingDao.saveOrUpdateShowing(showing);

	        System.out.println("Showing updated successfully.");
	    } else {
	        System.out.println("Showing with ID " + showingId + " not found.");
	    }
	}



	private static void addShowing(Scanner scanner, SessionFactory sessionFactory) {
        System.out.println("Adding a New Showing");
        
        System.out.print("Enter movie ID: ");
        long movieId = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter show time (yyyy-MM-dd HH:mm:ss): ");
        String showTimeStr = scanner.nextLine();
        Date showTime = null;
        try {
            showTime = DATE_FORMAT.parse(showTimeStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm:ss format.");
            return;
        }
        
        System.out.print("Enter available seats: ");
        int availableSeats = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        Movie movie = new Movie();
        movie.setId(movieId);
        
        Showing showing = new Showing();
        showing.setMovie(movie);
        showing.setShowTime(showTime);
        showing.setAvailableSeats(availableSeats);
        
        ShowingDao showingDao = new ShowingDaoImpl(sessionFactory);
        showingDao.saveOrUpdateShowing(showing);
        
        System.out.println("Showing added successfully.");
    }

	private static void deleteTheatre(Scanner scanner, SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			System.out.println("Deleting a Theatre");

			System.out.print("Enter theatre ID to delete: ");
			Long theatreId = scanner.nextLong();
			scanner.nextLine();

			Theatre theatre = session.get(Theatre.class, theatreId);

			if (theatre != null) {
				session.delete(theatre);
				transaction.commit();
				System.out.println("Theatre deleted successfully.");
			} else {
				System.out.println("Theatre with ID " + theatreId + " not found.");
			}
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private static void deleteMovie(Scanner scanner, SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			System.out.println("Deleting a Movie");

			System.out.print("Enter movie ID to delete: ");
			Long movieId = scanner.nextLong();
			scanner.nextLine(); // Consume the newline character

			// Retrieve the movie from the database
			Movie movie = session.get(Movie.class, movieId);

			if (movie != null) {
				// Delete the movie
				session.delete(movie);
				transaction.commit();
				System.out.println("Movie deleted successfully.");
			} else {
				System.out.println("Movie with ID " + movieId + " not found.");
			}
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private static void editTheatre(Scanner scanner, SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			System.out.println("Editing a Theatre");

			System.out.print("Enter theatre ID to edit: ");
			Long theatreId = scanner.nextLong();
			scanner.nextLine(); // Consume the newline character

			// Retrieve the theatre from the database
			Theatre theatre = session.get(Theatre.class, theatreId);

			if (theatre != null) {
				System.out.println("Current details of the theatre:");
				System.out.println(theatre);

				System.out.println("Enter new details:");

				System.out.print("Enter new name (leave empty to keep current): ");
				String newName = scanner.nextLine().trim();
				if (!newName.isEmpty()) {
					theatre.setName(newName);
				}

				System.out.print("Enter new city (leave empty to keep current): ");
				String newCity = scanner.nextLine().trim();
				if (!newCity.isEmpty()) {
					theatre.setCity(newCity);
				}

				System.out.print("Enter new address (leave empty to keep current): ");
				String newAddress = scanner.nextLine().trim();
				if (!newAddress.isEmpty()) {
					theatre.setAddress(newAddress);
				}

				System.out.print("Enter new type (leave empty to keep current): ");
				String newType = scanner.nextLine().trim();
				if (!newType.isEmpty()) {
					theatre.setType(newType);
				}

				// Update the theatre
				session.update(theatre);
				transaction.commit();
				System.out.println("Theatre updated successfully.");
			} else {
				System.out.println("Theatre with ID " + theatreId + " not found.");
			}
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private static void editMovie(Scanner scanner, SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			System.out.println("Editing a Movie");

			System.out.print("Enter movie ID to edit: ");
			Long movieId = scanner.nextLong();
			scanner.nextLine(); // Consume the newline character

			// Retrieve the movie from the database
			Movie movie = session.get(Movie.class, movieId);

			if (movie != null) {
				System.out.println("Current details of the movie:");
				System.out.println(movie);

				System.out.println("Enter new details:");

				System.out.print("Enter new title (leave empty to keep current): ");
				String newTitle = scanner.nextLine().trim();
				if (!newTitle.isEmpty()) {
					movie.setTitle(newTitle);
				}

				System.out.print("Enter new genres (leave empty to keep current): ");
				String newGenres = scanner.nextLine().trim();
				if (!newGenres.isEmpty()) {
					movie.setGenres(newGenres);
				}

				System.out.print("Enter new director (leave empty to keep current): ");
				String newDirector = scanner.nextLine().trim();
				if (!newDirector.isEmpty()) {
					movie.setDirector(newDirector);
				}

				System.out.print("Enter new year (leave empty to keep current): ");
				String newYearInput = scanner.nextLine().trim();
				if (!newYearInput.isEmpty()) {
					int newYear = Integer.parseInt(newYearInput);
					movie.setYear(newYear);
				}

				// Update the movie
				session.update(movie);
				transaction.commit();
				System.out.println("Movie updated successfully.");
			} else {
				System.out.println("Movie with ID " + movieId + " not found.");
			}
		} catch (HibernateException | NumberFormatException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private static void addTheatre(Scanner scanner, SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			System.out.println("Adding a New Theatre");

			System.out.print("Enter theatre name: ");
			String name = scanner.nextLine();

			System.out.print("Enter city: ");
			String city = scanner.nextLine();

			System.out.print("Enter address: ");
			String address = scanner.nextLine();

			System.out.print("Enter type: ");
			String type = scanner.nextLine();

			Theatre theatre = new Theatre(name, city, address, type);

			session.save(theatre);

			transaction.commit();
			System.out.println("Theatre added successfully.");
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private static void addMovie(Scanner scanner, SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			System.out.println("Adding a New Movie");

			System.out.print("Enter movie title: ");
			String title = scanner.nextLine();

			System.out.print("Enter genres: ");
			String genres = scanner.nextLine();

			System.out.print("Enter director: ");
			String director = scanner.nextLine();

			System.out.print("Enter year: ");
			int year = scanner.nextInt();
			scanner.nextLine();

			Movie movie = new Movie(title, genres, director, year);

			session.save(movie);

			transaction.commit();
			System.out.println("Movie added successfully.");
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}
