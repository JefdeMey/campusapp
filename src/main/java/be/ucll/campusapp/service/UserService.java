package be.ucll.campusapp.service;

import be.ucll.campusapp.model.User;
import be.ucll.campusapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor-injectie
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Alle gebruikers
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Één gebruiker op id
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Opslaan (create of update)
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Verwijderen
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

