package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByUsername(String username);

    User addUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    long countUsers();
}
