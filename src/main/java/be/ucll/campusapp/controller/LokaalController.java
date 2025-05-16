package be.ucll.campusapp.controller;

import be.ucll.campusapp.dto.LokaalUpdateDTO;
import be.ucll.campusapp.exception.EntityNotFoundException;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.service.LokaalService;
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
    @GetMapping
    public List<Lokaal> getAllLokalen() {
        return lokaalService.findAllLokalen();
    }

    // GET /lokalen/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Lokaal> getLokaalById(@PathVariable Long id) {
        Optional<Lokaal> lokaal = lokaalService.findLokaalById(id);
        if(lokaal.isEmpty()) {
            throw new EntityNotFoundException("Lokaal met ID " + id + " werd niet gevonden.");
        }
        return ResponseEntity.ok(lokaal.get());
    }

    // POST /lokalen
    @PostMapping
    public ResponseEntity<Lokaal> addLokaal(@Valid @RequestBody Lokaal lokaal) {
        Lokaal savedLokaal = lokaalService.saveLokaal(lokaal);
        return ResponseEntity.ok(savedLokaal);
    }

    // DELETE /lokalen/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLokaal(@PathVariable Long id) {
        lokaalService.deleteLokaal(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
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
}

