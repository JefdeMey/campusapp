package be.ucll.campusapp.integration;

import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.repository.CampusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 🔧 Laadt de volledige Spring context (services, controllers, enz.)
@SpringBootTest

// 🧪 Zorgt dat we MockMvc kunnen gebruiken om HTTP-verzoeken te simuleren
@AutoConfigureMockMvc
public class CampusControllerIntegrationTest {

    // Injecteert een MockMvc-instance om requests te testen zonder frontend
    @Autowired
    private MockMvc mockMvc;

    // Injecteert het echte repository zodat we testdata kunnen voorzien
    @Autowired
    private CampusRepository campusRepository;

    // 🔁 Voor elke test: database resetten en testdata aanmaken
    @BeforeEach
    public void setUp() {
        campusRepository.deleteAll(); // Alle vorige data verwijderen
        Campus testCampus = new Campus("LEUVEN", "Naamsestraat", 100); // Testcampus
        campusRepository.save(testCampus); // Opslaan in database
    }

    @Test
    public void testGetCampusByNaam_returnsCorrectCampus() throws Exception {
        // 🔎 Voer een GET-verzoek uit naar /campussen/LEUVEN
        mockMvc.perform(get("/campussen/LEUVEN")
                        .accept(MediaType.APPLICATION_JSON)) // Verwacht JSON terug
                .andExpect(status().isOk()) // ✅ Status 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // ✅ Inhoudstype JSON
                .andExpect(jsonPath("$.naam").value("LEUVEN")) // ✅ naam moet "LEUVEN" zijn
                .andExpect(jsonPath("$.adres").value("Naamsestraat")); // ✅ adres moet correct zijn
    }
}

