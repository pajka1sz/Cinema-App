package agh.to.lab.cinema.restController;

import agh.to.lab.cinema.model.roles.Role;
import agh.to.lab.cinema.model.roles.RoleType;
import agh.to.lab.cinema.model.users.CinemaUser;
import agh.to.lab.cinema.model.users.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;
    private static final String baseUrl = "http://localhost:8080/user";
    public static String getBaseUrl() {
        return baseUrl;
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<CinemaUser> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/add_test")
    public String addTestUser() {
        CinemaUser kowalski = userService.createUser("test2", "test", "test2", RoleType.USER);
        userService.addUser(kowalski);
        return "User added";
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<Optional<CinemaUser>> registerUser(@RequestBody CinemaUser user) {
        try {
            System.out.println(user);
            if (!CinemaUser.validateEmail(user)) {
                return new ResponseEntity<>(Optional.empty(), HttpStatus.valueOf(401));
            }
            CinemaUser createdUser = userService.createUser(user.getUsername(), user.getPassword(), user.getEmail(), RoleType.USER);
            if (createdUser.getUsername().equals("admin")
                    && createdUser.getEmail().equals("admin@agh.edu.pl"))
                createdUser.setRole(userService.getAdminRole());
            userService.addUser(createdUser);
            return new ResponseEntity<>(Optional.of(createdUser), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage().contains("username") + " " + e.getMessage().contains("email"));
            if (e.getMessage().contains("USERNAME NULLS FIRST"))
                return new ResponseEntity<>(Optional.empty(), HttpStatus.valueOf(402));
            if (e.getMessage().contains("EMAIL NULLS FIRST"))
                return new ResponseEntity<>(Optional.empty(), HttpStatus.valueOf(403));
            return new ResponseEntity<>(Optional.empty(), HttpStatus.valueOf(404));  //e.getMessage()
        }
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<Optional<CinemaUser>> loginUser(@RequestBody CinemaUser user) {
        List<CinemaUser> users = userService.getUsers();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (CinemaUser u : users) {
            if (u.getUsername().equals(user.getUsername()) && passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                return new ResponseEntity<>(Optional.of(u), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update_role/{id}")
    public String updateUserRole(@PathVariable Integer id, @RequestBody Role role) {
        try {
            CinemaUser userToUpdate = userService.getUserById(id);
            userToUpdate.setRole(role);
            System.out.println("User " + userToUpdate.getUsername() + " role " + userToUpdate.getRole().getRoleToString());
            userService.addUser(userToUpdate);
            System.out.println("User role updated");
            return "User role updated";
        } catch (EntityNotFoundException e) {
            System.out.println("User with id " + id + " not found");
            return "User with id " + id + " not found";
        } catch (Exception e) {
            System.out.println("Error updating user role");
            return "Error updating user role";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "User deleted";
    }
}
