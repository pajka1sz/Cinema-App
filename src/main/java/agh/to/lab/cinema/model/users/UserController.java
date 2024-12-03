package agh.to.lab.cinema.model.users;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
@RequestMapping(path = "/api")
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
        CinemaUser kowalski = new CinemaUser("test2", "test", "test2");
        userService.addUser(kowalski);
        return "User added";
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public String registerUser(@RequestBody CinemaUser user) {
        try {
            if (!CinemaUser.validateEmail(user)) {
                return "Invalid email";
            }
            userService.addUser(user);
            return "User added";
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage().contains("username") + " " + e.getMessage().contains("email"));
            if (e.getMessage().contains("USERNAME NULLS FIRST"))
                return "Username not available";
            if (e.getMessage().contains("EMAIL NULLS FIRST"))
                return "Email occupied";
            return e.getMessage();
        }
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public String loginUser(@RequestBody CinemaUser user) {
        List<CinemaUser> users = userService.getUsers();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (CinemaUser u : users) {
            if (u.getUsername().equals(user.getUsername()) && passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                return "User logged in";
            }
        }
        return "User not found";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "User deleted";
    }
}
