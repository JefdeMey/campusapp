package be.ucll.campusapp.controller;

import be.ucll.campusapp.dto.LokaalUpdateDTO;
import be.ucll.campusapp.dto.ReservatieDTO;
import be.ucll.campusapp.exception.EntityNotFoundException;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.service.LokaalService;
import be.ucll.campusapp.service.ReservatieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lokalen")
public class LokaalController {

    private final LokaalService lokaalService;

    public LokaalController(LokaalService lokaalService) {
        this.lokaalService = lokaalService;
    }

    // GET /lokalen
    @Operation(summary="Toon alle lokalen")
    @GetMapping
    public List<Lokaal> getAllLokalen() {
        return lokaalService.findAllLokalen();
    }

    // GET /lokalen/{id}
    @Operation(summary="Toon één lokaal")
    @GetMapping("/{id}")
    public ResponseEntity<Lokaal> getLokaalById(@PathVariable Long id) {
        Optional<Lokaal> lokaal = lokaalService.findLokaalById(id);
        if(lokaal.isEmpty()) {
            throw new EntityNotFoundException("Lokaal met ID " + id + " werd niet gevonden.");
        }
        return ResponseEntity.ok(lokaal.get());
    }

    // POST /lokalen
    @Operation(summary="Creëer een lokaal")
    @PostMapping
    public ResponseEntity<Lokaal> addLokaal(@Valid @RequestBody Lokaal lokaal) {
        Lokaal savedLokaal = lokaalService.saveLokaal(lokaal);
        return ResponseEntity.ok(savedLokaal);
    }

    // DELETE /lokalen/{id}
    @Operation(summary="Verwijder een lokaal op basis van id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLokaal(@PathVariable Long id) {
        lokaalService.deleteLokaal(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    @Operation(summary="Pas een lokaal aan op basis van id")
    public ResponseEntity<Lokaal> updateLokaal(@PathVariable Long id, @Valid @RequestBody LokaalUpdateDTO dto) {
        return lokaalService.findLokaalById(id)
                .map(bestaand -> {
                    bestaand.setNaam(dto.getNaam());
                    bestaand.setType(dto.getType());
                    bestaand.setAantalPersonen(dto.getAantalPersonen());
                    bestaand.setVoornaam(dto.getVoornaam());
                    bestaand.setAchternaam(dto.getAchternaam());
                    bestaand.setVerdieping(dto.getVerdieping());
                    return ResponseEntity.ok(lokaalService.saveLokaal(bestaand));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    //Extra filters
    @GetMapping("/campus/{naam}")
    public List<Lokaal> getLokalenByCampusNaam(@PathVariable String naam) {
        return lokaalService.findLokalenByCampusNaam(naam);
    }
    @GetMapping("/verdieping/{verdieping}/campus/{campusNaam}")
    public List<Lokaal> getLokalenByVerdiepingAndCampus(@PathVariable int verdieping, @PathVariable String campusNaam) {
        return lokaalService.findLokalenByVerdiepingAndCampus(verdieping, campusNaam);
    }

    @Tag(name = "Reservaties per lokaal")
    @RestController
    @RequestMapping("/campus/{campusNaam}/rooms")
    public class LokaalReservatieController {

        private final ReservatieService reservatieService;

        public LokaalReservatieController(ReservatieService reservatieService) {
            this.reservatieService = reservatieService;
        }

        @Operation(summary = "Alle reservaties ophalen voor een lokaal")
        @GetMapping("/{roomId}/reservaties")
        public ResponseEntity<List<ReservatieDTO>> getReservatiesForLokaal(
                @PathVariable String campusNaam,
                @PathVariable Long roomId
        ) {
            // Extra validatie op campus optioneel
            List<ReservatieDTO> reservaties = reservatieService.findByLokaalId(roomId);
            return ResponseEntity.ok(reservaties);
        }
    }

}

