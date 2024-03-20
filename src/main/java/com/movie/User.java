package com.movie;

import java.util.List;

import javax.persistence.*;

import org.hibernate.Session;


@Entity
@Table(name = "users") 
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "role", nullable = false)
    private int role;

    public User() {
    }

    public User(String username, String email, String password, String name, int role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
                + ", name=" + name + ", role=" + role + "]";
    }

    public List<Booking> getBookings(Session session) {
        List<Booking> bookings = session.createQuery(
            "SELECT b FROM Booking b JOIN FETCH b.user WHERE b.user.id = :userId",
            Booking.class
        )
        .setParameter("userId", this.id)
        .getResultList();
        return bookings;
    }




	
}
