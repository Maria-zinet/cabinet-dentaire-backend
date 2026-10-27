package com.cabinetdentaire.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.cabinetdentaire.entities.User;
import com.cabinetdentaire.services.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;


    // Injection du Service
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // =========================
    // GET : tous les users
    // =========================

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    // =========================
    // GET : user par ID
    // =========================

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

//nv//
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.login(user.getEmail(), user.getPassword());
    }
    // =========================
    // POST : créer un user
    // =========================

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }


    // =========================
    // PUT : modifier un user
    // =========================

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @RequestBody User user) {

        return userService.updateUser(id, user);
    }


    // =========================
    // DELETE : supprimer
    // =========================

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}