package be.ucll.campusapp.service;

import be.ucll.campusapp.dto.ReservatieCreateDTO;
import be.ucll.campusapp.dto.ReservatieUpdateDTO;
import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.model.Reservatie;
import be.ucll.campusapp.model.User;
import be.ucll.campusapp.repository.LokaalRepository;
import be.ucll.campusapp.repository.ReservatieRepository;
import be.ucll.campusapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservatieServiceTest {

    @Mock
    private ReservatieRepository reservatieRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LokaalRepository lokaalRepository;

    @InjectMocks
    private ReservatieService reservatieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ReservatieCreateDTO getValidCreateDTO() {
        ReservatieCreateDTO dto = new ReservatieCreateDTO();
        dto.setStartTijd(LocalDateTime.now().plusDays(1));
        dto.setEindTijd(LocalDateTime.now().plusDays(1).plusHours(2));
        dto.setAantalPersonen(10);
        dto.setGebruikerId(1L);
        dto.setLokaalIds(List.of(1L));
        return dto;
    }

    @Test
    void create_GebruikerNietGevonden_ThrowException() {
        ReservatieCreateDTO dto = getValidCreateDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> reservatieService.create(dto));
        assertTrue(ex.getMessage().contains("Gebruiker niet gevonden"));
    }

    @Test
    void create_LokaalNietGevonden_ThrowException() {
        ReservatieCreateDTO dto = getValidCreateDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(reservatieRepository.findByLokalen_IdAndStartTijdLessThanEqualAndEindTijdGreaterThanEqual(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(lokaalRepository.findAllById(any())).thenReturn(List.of()); // niks terug

        Exception ex = assertThrows(IllegalArgumentException.class, () -> reservatieService.create(dto));
        assertTrue(ex.getMessage().contains("lokalen bestaan niet"));
    }

    @Test
    void create_DubbeleLokaalIds_ThrowException() {
        ReservatieCreateDTO dto = getValidCreateDTO();
        dto.setLokaalIds(List.of(1L, 1L));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> reservatieService.create(dto));
        assertTrue(ex.getMessage().contains("niet meerdere keren"));
    }

    @Test
    void create_OverlappingReservatie_ThrowException() {
        ReservatieCreateDTO dto = getValidCreateDTO();
        Reservatie overlap = new Reservatie();

        when(reservatieRepository.findByLokalen_IdAndStartTijdLessThanEqualAndEindTijdGreaterThanEqual(anyLong(), any(), any()))
                .thenReturn(List.of(overlap));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> reservatieService.create(dto));
        assertTrue(ex.getMessage().contains("reeds gereserveerd"));
    }

    @Test
    void create_AantalPersonenTeGroot_ThrowException() {
        ReservatieCreateDTO dto = getValidCreateDTO();
        dto.setAantalPersonen(100); // te veel

        User user = new User();
        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);
        lokaal.setAantalPersonen(10);

        when(reservatieRepository.findByLokalen_IdAndStartTijdLessThanEqualAndEindTijdGreaterThanEqual(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(lokaalRepository.findAllById(List.of(1L))).thenReturn(List.of(lokaal));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> reservatieService.create(dto));
        assertTrue(ex.getMessage().contains("overschrijdt"));
    }

    @Test
    void create_Success() {
        ReservatieCreateDTO dto = getValidCreateDTO();

        User user = new User();
        user.setVoornaam("John");
        user.setAchternaam("Doe");

        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);
        lokaal.setAantalPersonen(20);
        lokaal.setNaam("Zaal A");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(lokaalRepository.findAllById(List.of(1L))).thenReturn(List.of(lokaal));
        when(reservatieRepository.findByLokalen_IdAndStartTijdLessThanEqualAndEindTijdGreaterThanEqual(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(reservatieRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = reservatieService.create(dto);

        assertEquals("John Doe", result.getGebruikerNaam());
        assertEquals(1, result.getLokaalNamen().size());
        assertEquals("Zaal A", result.getLokaalNamen().get(0));
    }

    @Test
    void voegLokaalToeAanReservatie_LokaalReedsToegevoegd_ThrowException() {
        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);

        Reservatie reservatie = new Reservatie();
        reservatie.setId(100L);
        reservatie.setStartTijd(LocalDateTime.now().plusDays(1));
        reservatie.setEindTijd(LocalDateTime.now().plusDays(1).plusHours(2));
        reservatie.setLokalen(new HashSet<>(Set.of(lokaal)));

        User gebruiker = new User();
        gebruiker.setId(1L);
        reservatie.setGebruiker(gebruiker);

        when(reservatieRepository.findById(100L)).thenReturn(Optional.of(reservatie));
        when(lokaalRepository.findById(1L)).thenReturn(Optional.of(lokaal));

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> reservatieService.voegLokaalToeAanReservatie(1L, 100L, 1L));
        assertTrue(ex.getMessage().contains("al gekoppeld"));
    }

    @Test
    void findByCampusAndLokaalId_NietZelfdeCampus_ThrowException() {
        Campus campus = new Campus();
        campus.setNaam("Leuven");

        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);
        lokaal.setCampus(campus);

        when(lokaalRepository.findById(1L)).thenReturn(Optional.of(lokaal));

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> reservatieService.findByCampusAndLokaalId("Diepenbeek", 1L));

        assertTrue(ex.getMessage().contains("Lokaal hoort niet bij campus Diepenbeek"),
                "Exception message moet aangeven dat het lokaal niet bij de gevraagde campus hoort");
    }

    @Test
    void update_ReservatieNietGevonden_ReturnsEmpty() {
        when(reservatieRepository.findById(1L)).thenReturn(Optional.empty());

        ReservatieUpdateDTO dto = new ReservatieUpdateDTO();
        dto.setStartTijd(LocalDateTime.now().plusDays(1));
        dto.setEindTijd(LocalDateTime.now().plusDays(1).plusHours(2));
        dto.setAantalPersonen(5);
        dto.setLokaalIds(List.of(1L));

        var result = reservatieService.update(1L, dto);
        assertTrue(result.isEmpty());
    }

    @Test
    void update_OverlappingMetAndereReservatie_ThrowsException() {
        Reservatie bestaande = new Reservatie();
        bestaande.setId(1L);

        Reservatie overlappende = new Reservatie();
        overlappende.setId(2L); // andere reservatie

        when(reservatieRepository.findById(1L)).thenReturn(Optional.of(bestaande));
        when(reservatieRepository.findByLokalen_IdAndStartTijdLessThanEqualAndEindTijdGreaterThanEqual(anyLong(), any(), any()))
                .thenReturn(List.of(overlappende));

        ReservatieUpdateDTO dto = new ReservatieUpdateDTO();
        dto.setStartTijd(LocalDateTime.now().plusDays(1));
        dto.setEindTijd(LocalDateTime.now().plusDays(1).plusHours(2));
        dto.setAantalPersonen(5);
        dto.setLokaalIds(List.of(1L));

        assertThrows(IllegalArgumentException.class, () -> reservatieService.update(1L, dto));
    }

    @Test
    void update_Geldig_ReturnsDTO() {
        Reservatie bestaande = new Reservatie();
        bestaande.setId(1L);
        bestaande.setGebruiker(new User());
        bestaande.setLokalen(new HashSet<>());
        bestaande.setStartTijd(LocalDateTime.now().plusDays(1));
        bestaande.setEindTijd(LocalDateTime.now().plusDays(1).plusHours(2));

        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);
        lokaal.setAantalPersonen(20);
        lokaal.setNaam("A1");

        when(reservatieRepository.findById(1L)).thenReturn(Optional.of(bestaande));
        when(lokaalRepository.findAllById(List.of(1L))).thenReturn(List.of(lokaal));
        when(reservatieRepository.findByLokalen_IdAndStartTijdLessThanEqualAndEindTijdGreaterThanEqual(anyLong(), any(), any()))
                .thenReturn(Collections.singletonList(bestaande)); // enkel zichzelf
        when(reservatieRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ReservatieUpdateDTO dto = new ReservatieUpdateDTO();
        dto.setStartTijd(LocalDateTime.now().plusDays(1));
        dto.setEindTijd(LocalDateTime.now().plusDays(1).plusHours(2));
        dto.setAantalPersonen(5);
        dto.setCommentaar("Update test");
        dto.setLokaalIds(List.of(1L));

        var result = reservatieService.update(1L, dto);
        assertTrue(result.isPresent());
        assertEquals("A1", result.get().getLokaalNamen().get(0));
    }
    @Test
    void delete_Success() {
        reservatieService.delete(1L);
        verify(reservatieRepository, times(1)).deleteById(1L);
    }
    @Test
    void findByGebruikerId_ReturnsList() {
        Reservatie r = new Reservatie();
        r.setId(1L);
        User u = new User();
        u.setVoornaam("A");
        u.setAchternaam("B");
        r.setGebruiker(u);
        r.setLokalen(new HashSet<>());

        when(reservatieRepository.findByGebruiker_IdOrderByStartTijdAsc(1L)).thenReturn(List.of(r));
        var result = reservatieService.findByGebruikerId(1L);
        assertEquals(1, result.size());
        assertEquals("A B", result.get(0).getGebruikerNaam());
    }
    @Test
    void findByLokaalId_ReturnsList() {
        Reservatie r = new Reservatie();
        r.setId(1L);
        User u = new User();
        u.setVoornaam("A");
        u.setAchternaam("B");
        r.setGebruiker(u);
        r.setLokalen(new HashSet<>());

        when(reservatieRepository.findByLokalen_Id(1L)).thenReturn(List.of(r));
        var result = reservatieService.findByLokaalId(1L);
        assertEquals(1, result.size());
    }

}