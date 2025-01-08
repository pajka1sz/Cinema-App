package agh.to.lab.cinema.model.users;

import agh.to.lab.cinema.model.roles.Role;
import agh.to.lab.cinema.model.roles.RoleRepository;
import agh.to.lab.cinema.model.roles.RoleType;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<CinemaUser> getUsers() {
        return userRepository.findAll();
    }

    public CinemaUser getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void addUser(CinemaUser user) {
        userRepository.save(user);
    }

    public CinemaUser getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public CinemaUser createUser(String username, String password, String email, RoleType roleType) {
        Optional<Role> roleOptional = roleRepository.findByRole(roleType);
        System.out.println("TU: " + roleOptional.isEmpty()+ ", " + roleOptional.toString() + ", " + roleOptional.get().toString());
        if (roleOptional.isEmpty()) {
            roleOptional = roleRepository.findByRole(RoleType.USER);
            if (roleOptional.isEmpty()) {
                throw new RuntimeException("Role not found: " + roleType);
            }
        }
        return userRepository.save(new CinemaUser(username, password, email, roleOptional.get()));
    }

    public Role getAdminRole() {
        return roleRepository.findByRole(RoleType.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));
    }

    public List<CinemaUser> getUsersByRole(RoleType roleType) {
        Optional<Role> roleOptional = roleRepository.findByRole(roleType);
        if (roleOptional.isEmpty()) {
            throw new RuntimeException("Role not found: " + roleType);
        }
        return userRepository.findByRole(roleOptional.get());
    }
}
