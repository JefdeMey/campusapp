package be.ucll.campusapp.service;

import be.ucll.campusapp.dto.LokaalCreateDTO;
import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.repository.CampusRepository;
import be.ucll.campusapp.repository.LokaalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LokaalServiceTest {

    @Mock
    private LokaalRepository lokaalRepository;

    @Mock
    private CampusRepository campusRepository;

    @InjectMocks
    private LokaalService lokaalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllLokalen() {
        when(lokaalRepository.findAll()).thenReturn(List.of(new Lokaal(), new Lokaal()));
        List<Lokaal> result = lokaalService.findAllLokalen();
        assertEquals(2, result.size());
    }

    @Test
    void testFindLokaalById() {
        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);
        when(lokaalRepository.findById(1L)).thenReturn(Optional.of(lokaal));
        Optional<Lokaal> result = lokaalService.findLokaalById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testSaveLokaal_NieuwLokaalMetBestaandeNaam() {
        Lokaal lokaal = new Lokaal();
        Campus campus = new Campus();
        campus.setNaam("UCLL");
        lokaal.setCampus(campus);
        lokaal.setNaam("A1");

        when(campusRepository.existsById("UCLL")).thenReturn(true);
        when(lokaalRepository.findByCampus_NaamAndNaam("UCLL", "A1")).thenReturn(Optional.of(new Lokaal()));

        assertThrows(IllegalArgumentException.class, () -> lokaalService.saveLokaal(lokaal));
    }

    @Test
    void testCreateLokaal_Success() {
        Campus campus = new Campus();
        campus.setNaam("UCLL");

        LokaalCreateDTO dto = new LokaalCreateDTO();
        dto.setNaam("A1");
        dto.setType("Leslokaal");
        dto.setAantalPersonen(25);
        dto.setVoornaam("Jan");
        dto.setAchternaam("Jansen");
        dto.setVerdieping(1);

        Lokaal expected = new Lokaal();
        expected.setNaam("A1");
        expected.setAantalPersonen(25);
        expected.setCampus(campus);

        when(campusRepository.findById("UCLL")).thenReturn(Optional.of(campus));
        when(lokaalRepository.findByCampus_NaamAndNaam("UCLL", "A1")).thenReturn(Optional.empty());
        when(lokaalRepository.save(any(Lokaal.class))).thenReturn(expected);

        Lokaal result = lokaalService.create("UCLL", dto);

        assertEquals("A1", result.getNaam());
        assertEquals(25, result.getAantalPersonen());
    }


    @Test
    void testUpdateLokaal_NotFound() {
        when(lokaalRepository.findById(1L)).thenReturn(Optional.empty());
        LokaalCreateDTO dto = new LokaalCreateDTO();
        assertThrows(EntityNotFoundException.class, () -> lokaalService.updateLokaal(1L, dto));
    }

    @Test
    void testDeleteLokaal_Success() {
        when(lokaalRepository.existsById(1L)).thenReturn(true);
        lokaalService.deleteLokaal(1L);
        verify(lokaalRepository).deleteById(1L);
    }

    @Test
    void testDeleteLokaal_NotFound() {
        when(lokaalRepository.existsById(1L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> lokaalService.deleteLokaal(1L));
    }

    @Test
    void testGetLokaalByIdOrThrow_NotFound() {
        when(lokaalRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> lokaalService.getLokaalByIdOrThrow(1L));
    }

    @Test
    void testFindLokalenByCampusNaam() {
        when(lokaalRepository.findByCampus_Naam("UCLL")).thenReturn(List.of(new Lokaal()));
        List<Lokaal> result = lokaalService.findLokalenByCampusNaam("UCLL");
        assertEquals(1, result.size());
    }

    @Test
    void testFindLokalenByVerdiepingAndCampus() {
        when(lokaalRepository.findByVerdiepingAndCampus_Naam(2, "UCLL")).thenReturn(List.of(new Lokaal()));
        List<Lokaal> result = lokaalService.findLokalenByVerdiepingAndCampus(2, "UCLL");
        assertEquals(1, result.size());
    }
}
