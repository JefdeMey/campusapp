package be.ucll.campusapp.controller;

import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.service.LokaalService;
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
        return lokaal.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /lokalen
    @PostMapping
    public Lokaal addLokaal(@RequestBody Lokaal lokaal) {
        return lokaalService.saveLokaal(lokaal);
    }

    // DELETE /lokalen/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLokaal(@PathVariable Long id) {
        lokaalService.deleteLokaal(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Lokaal> updateLokaal(@PathVariable Long id, @RequestBody Lokaal updatedLokaal) {
        Optional<Lokaal> bestaand = lokaalService.findLokaalById(id);
        if (bestaand.isPresent()) {
            updatedLokaal.setId(id);
            return ResponseEntity.ok(lokaalService.saveLokaal(updatedLokaal));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

