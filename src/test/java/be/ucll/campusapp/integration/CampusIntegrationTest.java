package be.ucll.campusapp.integration;

import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.repository.CampusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CampusIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
        Campus nieuw = new Campus("DIEST", "Stationsstraat", 20);

        mockMvc.perform(post("/campussen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nieuw)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam").value("DIEST"));
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

        Campus update = new Campus("DIEST", "Nieuwstraat", 55);

        mockMvc.perform(put("/campussen/DIEST")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adres").value("Nieuwstraat"))
                .andExpect(jsonPath("$.aantalParkeerplaatsen").value(55));
    }

}

