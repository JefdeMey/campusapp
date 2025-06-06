package be.ucll.campusapp.service;

import be.ucll.campusapp.model.User;
import be.ucll.campusapp.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker met ID " + id + " bestaat niet.");
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Gebruiker is nog gelinkt aan andere gegevens.");
        }
    }

    public List<User> findUsersByVoornaamOfAchternaam(String deelNaam) {
        return userRepository.findByVoornaamContainingIgnoreCaseOrAchternaamContainingIgnoreCase(deelNaam, deelNaam);
    }
}

