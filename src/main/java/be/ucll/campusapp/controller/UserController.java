package be.ucll.campusapp.controller;

import be.ucll.campusapp.model.User;
import be.ucll.campusapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Gebruikers", description = "Beheer van gebruikers")
@RestController
@RequestMapping("/gebruikers")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /gebruikers
    @Operation(summary = "Alle gebruikers ophalen")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    // GET /gebruikers/{id}
    @Operation(summary = "Gebruikers ophalen")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /gebruikers
    @Operation(summary = "Gebruiker aanmaken")
    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // PUT /gebruikers/{id}
    @Operation(summary = "Gebruiker updaten op basis van id")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.findUserById(id)
                .map(existing -> {
                    existing.setVoornaam(updatedUser.getVoornaam());
                    existing.setAchternaam(updatedUser.getAchternaam());
                    return ResponseEntity.ok(userService.saveUser(existing));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /gebruikers/{id}
    @Operation(summary = "Gebruiker verwijderen op basis van id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

