package agh.to.lab.cinema.model.users;

import agh.to.lab.cinema.model.roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<CinemaUser, Integer> {
    List<CinemaUser> findByRole(Role role);
}
