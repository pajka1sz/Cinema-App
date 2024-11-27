package agh.to.lab.cinema.model.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<CinemaUser> getUsers() {
        return userRepository.findAll();
    }

    public void addUser(CinemaUser user) {
        userRepository.save(CinemaUser.hashPassword(user));
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
