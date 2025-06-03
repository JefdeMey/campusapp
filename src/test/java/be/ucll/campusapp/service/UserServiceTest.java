package be.ucll.campusapp.service;

import be.ucll.campusapp.model.User;
import be.ucll.campusapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser(Long id, String voornaam, String achternaam, String mail, LocalDate geboortedatum) {
        User user = new User();
        user.setId(id);
        user.setVoornaam(voornaam);
        user.setAchternaam(achternaam);
        user.setMail(mail);
        user.setGeboortedatum(geboortedatum);
        return user;
    }

    @Test
    void findAllUsers_ReturnsList() {
        List<User> mockUsers = List.of(
                createUser(1L, "Jan", "Janssens", "jan@mail.com", LocalDate.of(2000, 1, 15)),
                createUser(2L, "Piet", "Pieters", "piet@mail.com", LocalDate.of(1999, 6, 1))
        );

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.findAllUsers();

        assertEquals(2, result.size());
        assertEquals("jan@mail.com", result.get(0).getMail());
        assertEquals(LocalDate.of(2000, 1, 15), result.get(0).getGeboortedatum());
    }

    @Test
    void findUserById_BestaandeId_ReturnsUser() {
        User user = createUser(1L, "Emma", "Peeters", "emma@mail.com", LocalDate.of(1995, 3, 12));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("Emma", result.get().getVoornaam());
        assertEquals("emma@mail.com", result.get().getMail());
    }

    @Test
    void findUserById_OnbestaandeId_ReturnsEmpty() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void saveUser_NieuweOfBestaandeUser_ReturnsSavedUser() {
        User user = createUser(null, "Lotte", "Vermeulen", "lotte@mail.com", LocalDate.of(1990, 10, 5));

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertNotNull(result);
        assertEquals("Lotte", result.getVoornaam());
        assertEquals("lotte@mail.com", result.getMail());
    }

    @Test
    void deleteUser_GeldigeId_Verified() {
        Long userId = 3L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void findUsersByVoornaamOfAchternaam_MatchendeNaam_ReturnsList() {
        List<User> mockUsers = List.of(
                createUser(1L, "Tina", "Martens", "tina@mail.com", LocalDate.of(1998, 2, 20)),
                createUser(2L, "Martine", "Dupont", "martine@mail.com", LocalDate.of(1997, 7, 11))
        );

        when(userRepository.findByVoornaamContainingIgnoreCaseOrAchternaamContainingIgnoreCase("mart", "mart"))
                .thenReturn(mockUsers);

        List<User> result = userService.findUsersByVoornaamOfAchternaam("mart");

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getMail().equals("tina@mail.com")));
    }
}
