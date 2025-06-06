package be.ucll.campusapp.integration;

import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.repository.CampusRepository;
import be.ucll.campusapp.repository.LokaalRepository;
import be.ucll.campusapp.repository.ReservatieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//alle functionaliteit en samenwerking tussen lagen wil testen (eind-tot-eind)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Zorgt dat application-test.properties gebruikt wordt (met H2)
public class LokaalIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservatieRepository reservatieRepository;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private LokaalRepository lokaalRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        reservatieRepository.deleteAll();
        lokaalRepository.deleteAll();
        campusRepository.deleteAll();
    }

    @Test
    void testCreateLokaalVoorBestaandeCampus() throws Exception {
        // 1. Voeg een campus toe in de H2-database
        Campus campus = new Campus("HASSELT", "Elfde-Liniestraat", 100);
        campusRepository.save(campus);

        // 2. Bouw de JSON-payload voor het lokaal (zonder campusId hierin!)
        String lokaalJson = """
                {
                    "naam": "1.01",
                    "type": "Leslokaal",
                    "aantalPersonen": 30,
                    "voornaam": "Jan",
                    "achternaam": "Janssens",
                    "verdieping": 0
                }
                """;

        // 3. Stuur POST-request naar correcte URL met campusId in het pad
        mockMvc.perform(post("/lokalen/campus/HASSELT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lokaalJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.naam", is("1.01")))
                .andExpect(jsonPath("$.type", is("Leslokaal")))
                .andExpect(jsonPath("$.aantalPersonen", is(30)))
                .andExpect(jsonPath("$.voornaam", is("Jan")))
                .andExpect(jsonPath("$.achternaam", is("Janssens")))
                .andExpect(jsonPath("$.verdieping", is(0)))
                .andExpect(jsonPath("$.campusNaam", is("HASSELT")));
    }

    @Test
    void testUpdateLokaalWijzigtBestaandLokaal() throws Exception {
        // 1. Campus opslaan
        Campus campus = new Campus("HASSELT", "Elfde-Liniestraat", 100);
        campusRepository.save(campus);

        // 2. Een lokaal opslaan
        Lokaal lokaal = new Lokaal("1.01", "Leslokaal", 30, "Jan", "Janssens", 0, campus);
        lokaalRepository.save(lokaal);

        // 3. JSON met gewijzigde waarden (zelfde campus)
        String updateJson = """
            {
                "naam": "1.02",
                "type": "Vergaderzaal",
                "aantalPersonen": 12,
                "voornaam": "Klaas",
                "achternaam": "Peeters",
                "verdieping": 1
            }
            """;

        // 4. Verstuur PUT-request
        mockMvc.perform(put("/lokalen/" + lokaal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam", is("1.02")))
                .andExpect(jsonPath("$.type", is("Vergaderzaal")))
                .andExpect(jsonPath("$.aantalPersonen", is(12)))
                .andExpect(jsonPath("$.voornaam", is("Klaas")))
                .andExpect(jsonPath("$.achternaam", is("Peeters")))
                .andExpect(jsonPath("$.verdieping", is(1)))
                .andExpect(jsonPath("$.campusNaam", is("HASSELT")));
    }

    @Test
    void testDeleteLokaal_verwijdertLokaalCorrect() throws Exception {
        // 1. Maak een campus aan
        Campus campus = new Campus("HASSELT", "Elfde-Liniestraat", 100);
        campusRepository.save(campus);

        // 2. Voeg een lokaal toe
        Lokaal lokaal = new Lokaal("1.02", "PC-lokaal", 20, "Lisa", "Vermeulen", 1, campus);
        lokaalRepository.save(lokaal);

        // 3. DELETE request uitvoeren
        mockMvc.perform(delete("/lokalen/" + lokaal.getId()))
                .andExpect(status().isNoContent());

        // 4. Verifiëren dat het lokaal verwijderd is
        boolean bestaatNog = lokaalRepository.findById(lokaal.getId()).isPresent();
        assert !bestaatNog;
    }

}
