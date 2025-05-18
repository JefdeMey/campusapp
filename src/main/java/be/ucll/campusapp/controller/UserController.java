package be.ucll.campusapp.controller;

import be.ucll.campusapp.dto.ReservatieDTO;
import be.ucll.campusapp.dto.UserCreateDTO;
import be.ucll.campusapp.dto.UserDTO;
import be.ucll.campusapp.dto.UserUpdateDTO;
import be.ucll.campusapp.model.User;
import be.ucll.campusapp.service.ReservatieService;
import be.ucll.campusapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Gebruikers", description = "Beheer van gebruikers")
@RestController
@RequestMapping("/gebruikers")
public class UserController {

    private final UserService userService;
    private final ReservatieService reservatieService;

    public UserController(UserService userService, ReservatieService reservatieService) {
        this.userService = userService;
        this.reservatieService = reservatieService;
    }

    // GET /gebruikers
    @Operation(summary = "Alle gebruikers ophalen")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.findAllUsers().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // GET /gebruikers/{id}
    @Operation(summary = "Gebruiker ophalen op basis van ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(value -> ResponseEntity.ok(mapToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /gebruikers
    @Operation(summary = "Nieuwe gebruiker aanmaken")
    @PostMapping
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserCreateDTO dto) {
        User user = new User();
        user.setVoornaam(dto.getVoornaam());
        user.setAchternaam(dto.getAchternaam());
        user.setMail(dto.getMail());
        user.setGeboortedatum(dto.getGeboortedatum());

        User saved = userService.saveUser(user);
        return ResponseEntity.ok(mapToDTO(saved));
    }

    // PUT /gebruikers/{id}
    @Operation(summary = "Gebruiker bijwerken op basis van ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        return userService.findUserById(id)
                .map(existing -> {
                    existing.setVoornaam(dto.getVoornaam());
                    existing.setAchternaam(dto.getAchternaam());
                    existing.setMail(dto.getMail());
                    existing.setGeboortedatum(dto.getGeboortedatum());
                    User updated = userService.saveUser(existing);
                    return ResponseEntity.ok(mapToDTO(updated));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /gebruikers/{id}
    @Operation(summary = "Gebruiker verwijderen op basis van ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Mapping van model naar DTO
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setVoornaam(user.getVoornaam());
        dto.setAchternaam(user.getAchternaam());
        dto.setMail(user.getMail());
        dto.setGeboortedatum(user.getGeboortedatum());
        return dto;
    }


    //haal de reservaties van een gebruiker op
    @Operation(summary = "Alle reservaties van een gebruiker ophalen")
    @GetMapping("/{id}/reservaties")
    public ResponseEntity<List<ReservatieDTO>> getReservatiesForUser(@PathVariable Long id) {
        if (!userService.findUserById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<ReservatieDTO> reservaties = reservatieService.findByGebruikerId(id);
        return ResponseEntity.ok(reservaties);
    }
}
