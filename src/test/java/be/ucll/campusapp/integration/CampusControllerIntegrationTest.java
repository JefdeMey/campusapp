package be.ucll.campusapp.integration;

import be.ucll.campusapp.dto.CampusCreateDTO;
import be.ucll.campusapp.repository.CampusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//validatie en gedrag van de controller test
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // gebruikt application-test.properties
class CampusControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postCampus_Returns201AndCorrectBody() throws Exception {
        // Arrange
        CampusCreateDTO dto = new CampusCreateDTO();
        dto.setNaam("Leuven");
        dto.setAdres("Naamsestraat 100");
        dto.setAantalParkeerPlaatsen(20);

        // Act + Assert
        mockMvc.perform(post("/campussen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.naam", is("Leuven")))
                .andExpect(jsonPath("$.adres", is("Naamsestraat 100")))
                .andExpect(jsonPath("$.aantalParkeerplaatsen", is(20)))
                .andExpect(jsonPath("$.aantalLokalen", is(0))); // bij creatie nog geen lokalen
    }
    @Test
    void postCampus_ZonderNaam_Returns400BadRequest() throws Exception {
        CampusCreateDTO dto = new CampusCreateDTO();
        dto.setNaam(""); // Leeg = ongeldig
        dto.setAdres("Naamsestraat 100");
        dto.setAantalParkeerPlaatsen(10);

        mockMvc.perform(post("/campussen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postCampus_ZonderAdres_Returns400BadRequest() throws Exception {
        CampusCreateDTO dto = new CampusCreateDTO();
        dto.setNaam("Leuven");
        dto.setAdres(""); // Leeg = ongeldig
        dto.setAantalParkeerPlaatsen(10);

        mockMvc.perform(post("/campussen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void postCampus_MetDubbeleNaam_ReturnsConflict() throws Exception {
        CampusCreateDTO dto = new CampusCreateDTO();
        dto.setNaam("Hasselt");
        dto.setAdres("Elfde-Liniestraat 10");
        dto.setAantalParkeerPlaatsen(30);

        // Eerste POST: succesvol
        mockMvc.perform(post("/campussen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Tweede POST met exact dezelfde data
        mockMvc.perform(post("/campussen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()); // of isBadRequest() als je dat gebruikt
    }

}
