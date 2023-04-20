package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The UserController class defines endpoints for creating and deleting users.
 * 
 * This class uses the Spring Framework's @RestController annotation, allowing
 * it to handle incoming HTTP requests and send back responses.
 * The class relies on the UserRepository interface to access the database and
 * perform CRUD (create, read, update, delete) operations on User entities.
 * Endpoints:
 * PUT /user: creates a new user with the provided name and email and saves it to the database.
 * DELETE /user: deletes a user with the provided ID from the database.
 */
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    /**
     * Creates a new user with the provided name and email
     * and saves it to the database.
     * 
     * @param payload the payload containing the name and email of the user
     * @return the ID of the newly created user
     */
    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        User user = new User(payload.getName(), payload.getEmail());
        userRepository.save(user);
        return ResponseEntity.ok(user.getId());
    }

    /**
     * Deletes a user with the provided ID from the database.
     * 
     * @param userId the ID of the user to be deleted
     * @return a response indicating whether the user was deleted successfully or if the user does not exist
     */
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("User with ID " + userId + " does noty exist.");
        }
    }
}
