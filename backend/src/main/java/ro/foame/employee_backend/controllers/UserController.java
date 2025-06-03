package ro.foame.employee_backend.controllers;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.foame.employee_backend.entities.User;
import ro.foame.employee_backend.repositories.UserRepository;
import ro.foame.employee_backend.services.UserService;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        return ResponseEntity.ok().body(currentUser);
    }
    @GetMapping("/")
    public ResponseEntity<List<User>> AllUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

}
