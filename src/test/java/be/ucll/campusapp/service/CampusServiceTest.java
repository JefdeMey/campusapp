package be.ucll.campusapp.service;

import be.ucll.campusapp.exception.EntityNotFoundException;
import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.repository.CampusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CampusServiceTest {

    @Mock
    private CampusRepository campusRepository;

    @InjectMocks
    private CampusService campusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testFindAllCampussen_returnsAllCampussen() {
        // Arrange
        Campus c1 = new Campus("LEUVEN", "Naamsestraat", 100);
        Campus c2 = new Campus("HASSELT", "Elfde Liniestraat", 80);
        when(campusRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        // Act
        List<Campus> result = campusService.findAllCampussen();

        // Assert
        assertEquals(2, result.size());
        assertEquals("LEUVEN", result.get(0).getNaam());
        verify(campusRepository, times(1)).findAll();
    }
    @Test
    void testFindCampusByNaam_geeftCampusTerugAlsDieBestaat() {
        // Arrange
        Campus campus = new Campus("LEUVEN", "Naamsestraat 1", 120);
        when(campusRepository.findById("LEUVEN")).thenReturn(Optional.of(campus));

        // Act
        Optional<Campus> resultaat = campusService.findCampusByNaam("LEUVEN");

        // Assert
        assertTrue(resultaat.isPresent());
        assertEquals("LEUVEN", resultaat.get().getNaam());
        verify(campusRepository).findById("LEUVEN"); // controleer of de repo werd aangesproken
    }
    @Test
    void testFindCampusByNaam_returnsEmptyIfNotFound() {
        // Arrange
        when(campusRepository.findById("HASSELT")).thenReturn(Optional.empty());

        // Act
        Optional<Campus> result = campusService.findCampusByNaam("HASSELT");

        // Assert
        assertFalse(result.isPresent());
        verify(campusRepository).findById("HASSELT");
    }
    @Test
    void testFindCampusByNaam_returnOptional() {
        // Arrange
        Campus campus = new Campus("LEUVEN", "Naamsestraat 1", 120);
        when(campusRepository.findById("LEUVEN")).thenReturn(Optional.of(campus));

        // Act
        Optional<Campus> result = campusService.findCampusByNaam("LEUVEN");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("LEUVEN", result.get().getNaam());
    }
    @Test
    void testSaveCampus_returnsSavedCampus() {
        // Arrange
        Campus campus = new Campus("HASSELT", "Elfde-Liniestraat 24", 50);
        when(campusRepository.save(campus)).thenReturn(campus);

        // Act
        Campus result = campusService.saveCampus(campus);

        // Assert
        assertNotNull(result);
        assertEquals("HASSELT", result.getNaam());
        verify(campusRepository, times(1)).save(campus);
    }
    @Test
    void testDeleteCampus_deletesCampusById() {
        // Act
        campusService.deleteCampus("LEUVEN");

        // Assert
        verify(campusRepository, times(1)).deleteById("LEUVEN");
    }

    @Test
    void testGetCampusByNaamOrThrow_returnsCampusIfExists() {
        // Arrange
        Campus campus = new Campus("LEUVEN", "Naamsestraat", 100);
        when(campusRepository.findById("LEUVEN")).thenReturn(Optional.of(campus));

        // Act
        Campus result = campusService.getCampusByNaamOrThrow("LEUVEN");

        // Assert
        assertEquals("LEUVEN", result.getNaam());
        verify(campusRepository, times(1)).findById("LEUVEN");
    }

    @Test
    void testGetCampusByNaamOrThrow_throwsExceptionIfNotFound() {
        // Arrange
        when(campusRepository.findById("HASSELT")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            campusService.getCampusByNaamOrThrow("HASSELT");
        });

        assertEquals("Campus 'HASSELT' werd niet gevonden.", thrown.getMessage());
        verify(campusRepository, times(1)).findById("HASSELT");
    }
}

