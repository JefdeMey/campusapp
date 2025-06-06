package be.ucll.campusapp.integration;

import be.ucll.campusapp.dto.CampusCreateDTO;
import be.ucll.campusapp.model.Campus;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//alle functionaliteit en samenwerking tussen lagen wil testen (eind-tot-eind)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CampusIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservatieRepository reservatieRepository;

    @Autowired
    private LokaalRepository lokaalRepository;

    @Autowired
    private CampusRepository campusRepository;

    @BeforeEach
    void setup() {
        reservatieRepository.deleteAll(); // eerst de koppelingen (kind)
        lokaalRepository.deleteAll();     // dan het lokaal (ouder)
        campusRepository.deleteAll();     // dan de campus
    }

    @Test
    void testGetAllCampussen_returnsOk() throws Exception {
        campusRepository.deleteAll(); // schone test
        campusRepository.save(new Campus("LEUVEN", "Naamsestraat", 50));
        campusRepository.save(new Campus("HASSELT", "Elfde-Liniestraat", 30));

        mockMvc.perform(get("/campussen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testPostCampus_createsNewCampus() throws Exception {
        // Maak een CampusCreateDTO als een Java Map, of als aparte DTO-klasse als je constructor hebt
        String campusJson = """
            {
              "naam": "DIEST",
              "adres": "Stationsstraat",
              "aantalParkeerPlaatsen": 20
            }
            """;

        mockMvc.perform(post("/campussen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(campusJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.naam").value("DIEST"))
                .andExpect(jsonPath("$.adres").value("Stationsstraat"))
                .andExpect(jsonPath("$.aantalParkeerplaatsen").value(20));
    }

    @Test
    void testGetCampusByNaam_returnsCorrectCampus() throws Exception {
        campusRepository.deleteAll(); // zorg voor een schone slate
        campusRepository.save(new Campus("LEUVEN", "Naamsestraat", 100));

        mockMvc.perform(get("/campussen/LEUVEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam").value("LEUVEN"))
                .andExpect(jsonPath("$.adres").value("Naamsestraat"))
                .andExpect(jsonPath("$.aantalParkeerplaatsen").value(100));
    }

    @Test
    void testDeleteCampus_removesCampus() throws Exception {
        campusRepository.deleteAll();
        campusRepository.save(new Campus("HASSELT", "Elfde-Liniestraat", 50));

        mockMvc.perform(delete("/campussen/HASSELT"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testPutCampus_updatesAdresEnParkeerplaatsen() throws Exception {
        campusRepository.deleteAll();
        campusRepository.save(new Campus("DIEST", "Stationsstraat", 20));

        CampusCreateDTO update = new CampusCreateDTO();
        update.setNaam("DIEST");
        update.setAdres("Nieuwstraat");
        update.setAantalParkeerPlaatsen(55);

        mockMvc.perform(put("/campussen/DIEST")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adres").value("Nieuwstraat"))
                .andExpect(jsonPath("$.aantalParkeerplaatsen").value(55));
    }

}

