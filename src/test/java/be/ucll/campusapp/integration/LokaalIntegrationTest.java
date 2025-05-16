package be.ucll.campusapp.integration;

import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.repository.CampusRepository;
import be.ucll.campusapp.repository.LokaalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LokaalIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private LokaalRepository lokaalRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        lokaalRepository.deleteAll();
        campusRepository.deleteAll();
    }

    @Test
    void testPostLokaal_createsNewLokaal() throws Exception {
        // Stap 1: Zorg dat de campus vooraf bestaat in de DB
        Campus campus = new Campus("HASSELT", "Elfde-Liniestraat", 50);
        campusRepository.save(campus);

        // Stap 2: Bouw JSON met enkel een referentie naar campus.naam
        String json = """
                {
                    "naam": "1.01",
                    "type": "Leslokaal",
                    "aantalPersonen": 30,
                    "voornaam": "Jan",
                    "achternaam": "Janssens",
                    "verdieping": 0,
                    "campus": {
                        "naam": "HASSELT"
                    }
                }
                """;

        // Stap 3: Stuur POST request
        mockMvc.perform(post("/lokalen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam", is("1.01")))
                .andExpect(jsonPath("$.campus.naam", is("HASSELT")));
    }
}
