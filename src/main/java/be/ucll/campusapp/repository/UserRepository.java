package be.ucll.campusapp.repository;

import be.ucll.campusapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByVoornaamContainingIgnoreCaseOrAchternaamContainingIgnoreCase(String voornaam, String achternaam);
}

