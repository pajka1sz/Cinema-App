package agh.to.lab.cinema.model.users;

import agh.to.lab.cinema.model.roles.RoleType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;

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

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "User deleted";
    }
}
