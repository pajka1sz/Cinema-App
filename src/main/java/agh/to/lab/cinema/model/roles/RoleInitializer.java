package agh.to.lab.cinema.model.roles;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByRole(RoleType.ADMIN).isEmpty()) {
            roleRepository.save(new Role(RoleType.ADMIN));
        }

        if (roleRepository.findByRole(RoleType.USER).isEmpty()) {
            roleRepository.save(new Role(RoleType.USER));
        }
    }
}
