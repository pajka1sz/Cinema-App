package agh.to.lab.cinema.model.users;

import agh.to.lab.cinema.model.roles.Role;
import agh.to.lab.cinema.model.roles.RoleRepository;
import agh.to.lab.cinema.model.roles.RoleType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserInitializer(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        Optional<Role> optionalRole = roleRepository.findByRole(RoleType.ADMIN);
        if (optionalRole.isPresent()) {
            if (userRepository.findByRole(optionalRole.get()).isEmpty()) {
                userRepository.save(new CinemaUser("admin", "admin", "admin@agh.edu.pl", optionalRole.get()));
            }
        }
    }
}
