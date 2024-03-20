package com.movie.dao;

import java.util.List;
import com.movie.User;

public interface UserDao {
    void saveOrUpdateUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    void deleteUser(Long id);
	User authenticateUser(String username, String password);
}

