package be.ucll.campusapp.service;

import be.ucll.campusapp.dto.CampusCreateDTO;
import be.ucll.campusapp.dto.LokaalDTO;
import be.ucll.campusapp.exception.EntityNotFoundException;
import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.model.Reservatie;
import be.ucll.campusapp.repository.CampusRepository;
import be.ucll.campusapp.repository.LokaalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CampusServiceTest {

    @Mock
    private CampusRepository campusRepository;

    @Mock
    private LokaalRepository lokaalRepository;

    @InjectMocks
    private CampusService campusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCampus() {
        CampusCreateDTO dto = new CampusCreateDTO();
        dto.setNaam("TestCampus");
        dto.setAdres("TestStraat 1");
        dto.setAantalParkeerPlaatsen(50);

        Campus savedCampus = new Campus();
        savedCampus.setNaam(dto.getNaam());
        savedCampus.setAdres(dto.getAdres());
        savedCampus.setAantalParkeerplaatsen(dto.getAantalParkeerPlaatsen());

        when(campusRepository.save(any(Campus.class))).thenReturn(savedCampus);

        Campus result = campusService.create(dto);

        assertEquals("TestCampus", result.getNaam());
        assertEquals("TestStraat 1", result.getAdres());
        assertEquals(50, result.getAantalParkeerplaatsen());
    }

    @Test
    void testUpdateCampusThrowsIfNotFound() {
        when(campusRepository.findById("NietBestaat")).thenReturn(Optional.empty());

        CampusCreateDTO dto = new CampusCreateDTO();
        dto.setAdres("NieuwAdres");
        dto.setAantalParkeerPlaatsen(100);

        assertThrows(EntityNotFoundException.class, () -> campusService.updateCampus("NietBestaat", dto));
    }

    @Test
    void testUpdateCampusSuccess() {
        Campus bestaande = new Campus();
        bestaande.setNaam("TestCampus");
        bestaande.setAdres("OudAdres");
        bestaande.setAantalParkeerplaatsen(30);

        when(campusRepository.findById("TestCampus")).thenReturn(Optional.of(bestaande));
        when(campusRepository.save(any(Campus.class))).thenAnswer(i -> i.getArgument(0));

        CampusCreateDTO dto = new CampusCreateDTO();
        dto.setAdres("NieuwAdres");
        dto.setAantalParkeerPlaatsen(80);

        Campus result = campusService.updateCampus("TestCampus", dto);

        assertEquals("NieuwAdres", result.getAdres());
        assertEquals(80, result.getAantalParkeerplaatsen());
    }

    @Test
    void testFindAllCampussen() {
        List<Campus> mockList = List.of(new Campus(), new Campus());
        when(campusRepository.findAll()).thenReturn(mockList);

        List<Campus> result = campusService.findAllCampussen();
        assertEquals(2, result.size());
    }

    @Test
    void testGetCampusByNaamOrThrow_Success() {
        Campus campus = new Campus();
        campus.setNaam("UCLL");
        when(campusRepository.findById("UCLL")).thenReturn(Optional.of(campus));

        Campus result = campusService.getCampusByNaamOrThrow("UCLL");
        assertEquals("UCLL", result.getNaam());
    }

    @Test
    void testGetCampusByNaamOrThrow_NotFound() {
        when(campusRepository.findById("Fake")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> campusService.getCampusByNaamOrThrow("Fake"));
    }

    @Test
    void testDeleteCampus() {
        campusService.deleteCampus("UCLL");
        verify(campusRepository).deleteById("UCLL");
    }

    @Test
    void testGetLokaalBinnenCampus_Success() {
        Campus campus = new Campus();
        campus.setNaam("UCLL");

        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);
        lokaal.setCampus(campus);

        when(lokaalRepository.findById(1L)).thenReturn(Optional.of(lokaal));

        Lokaal result = campusService.getLokaalBinnenCampus("UCLL", 1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetLokaalBinnenCampus_LokaalNotFound() {
        when(lokaalRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> campusService.getLokaalBinnenCampus("UCLL", 1L));
    }

    @Test
    void testGetLokaalBinnenCampus_CampusMismatch() {
        Campus andereCampus = new Campus();
        andereCampus.setNaam("Andere");

        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);
        lokaal.setCampus(andereCampus);

        when(lokaalRepository.findById(1L)).thenReturn(Optional.of(lokaal));

        assertThrows(IllegalArgumentException.class, () -> campusService.getLokaalBinnenCampus("UCLL", 1L));
    }

    @Test
    void testFindLokalenMetFilters_enkelMinSeats() {
        Lokaal l1 = new Lokaal(); l1.setAantalPersonen(20);
        Lokaal l2 = new Lokaal(); l2.setAantalPersonen(10);
        List<Lokaal> lokalen = List.of(l1, l2);

        when(lokaalRepository.findByCampus_Naam("UCLL")).thenReturn(lokalen);

        List<LokaalDTO> result = campusService.findLokalenMetFilters("UCLL", null, null, 15);
        assertEquals(1, result.size());
        assertEquals(20, result.get(0).getAantalPersonen());
    }

    @Test
    void testFindCampusByNaam() {
        Campus campus = new Campus();
        campus.setNaam("Test");
        when(campusRepository.findById("Test")).thenReturn(Optional.of(campus));

        Optional<Campus> result = campusService.findCampusByNaam("Test");
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getNaam());
    }
}
