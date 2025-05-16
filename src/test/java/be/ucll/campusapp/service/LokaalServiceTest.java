
package be.ucll.campusapp.service;

import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.repository.CampusRepository;
import be.ucll.campusapp.repository.LokaalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LokaalServiceTest {

    @Mock
    private LokaalRepository lokaalRepository;

    @Mock
    private CampusRepository campusRepository;

    @InjectMocks
    private LokaalService lokaalService;

    private Campus campus;

    @BeforeEach
    void setup() {
        campus = new Campus("LEUVEN", "Naamsestraat", 100);
    }

    @Test
    void testFindLokaalById_returnsLokaal() {
        Lokaal lokaal = new Lokaal("1.01", "Leslokaal", 30, "Jan", "Peeters", 1, campus);
        lokaal.setId(1L);
        when(lokaalRepository.findById(1L)).thenReturn(Optional.of(lokaal));

        Optional<Lokaal> result = lokaalService.findLokaalById(1L);

        assertTrue(result.isPresent());
        assertEquals("1.01", result.get().getNaam());
    }

    @Test
    void testFindAllLokalen_returnsAll() {
        Lokaal l1 = new Lokaal("1.01", "Leslokaal", 30, "Jan", "Peeters", 1, campus);
        Lokaal l2 = new Lokaal("1.02", "PC-lokaal", 20, "Lisa", "Vermeulen", 1, campus);
        when(lokaalRepository.findAll()).thenReturn(Arrays.asList(l1, l2));

        List<Lokaal> result = lokaalService.findAllLokalen();

        assertEquals(2, result.size());
    }

    @Test
    void testSaveLokaal_throwsIfCampusNotFound() {
        Lokaal lokaal = new Lokaal("1.03", "Leslokaal", 20, "Tom", "Janssens", 2, new Campus("HASSELT", "", 0));
        when(campusRepository.existsById("HASSELT")).thenReturn(false);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> lokaalService.saveLokaal(lokaal));
        assertEquals("De opgegegeven campus HASSELT bestaat niet!", ex.getMessage());
    }

    @Test
    void testSaveLokaal_throwsIfDuplicateLokaalInCampus() {
        Lokaal lokaal = new Lokaal("1.01", "Leslokaal", 25, "Jef", "De Mey", 1, campus);
        when(campusRepository.existsById("LEUVEN")).thenReturn(true);
        when(lokaalRepository.findByCampus_NaamAndNaam("LEUVEN", "1.01")).thenReturn(Optional.of(lokaal));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> lokaalService.saveLokaal(lokaal));
        assertEquals("Lokaal met deze naam bestaat al in deze campus.", ex.getMessage());
    }

    @Test
    void testDeleteLokaal_callsRepository() {
        lokaalService.deleteLokaal(5L);
        verify(lokaalRepository).deleteById(5L);
    }

    @Test
    void testFindLokalenByVerdiepingAndCampus_returnsFiltered() {
        Lokaal l1 = new Lokaal("2.01", "Auditorium", 80, "Inge", "Claes", 2, campus);
        when(lokaalRepository.findByVerdiepingAndCampus_Naam(2, "LEUVEN")).thenReturn(List.of(l1));

        List<Lokaal> result = lokaalService.findLokalenByVerdiepingAndCampus(2, "LEUVEN");

        assertEquals(1, result.size());
        assertEquals("2.01", result.get(0).getNaam());
    }
}
