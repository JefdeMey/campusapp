package be.ucll.campusapp.controller;

import be.ucll.campusapp.model.Reservatie;
import be.ucll.campusapp.service.ReservatieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reservaties", description = "Beheer van reservaties")
@RestController
@RequestMapping("/reservaties")
public class ReservatieController {

    private final ReservatieService reservatieService;

    public ReservatieController(ReservatieService reservatieService) {
        this.reservatieService = reservatieService;
    }

    // GET /reservaties
    @Operation(summary = "Alle reservaties ophalen")
    @GetMapping
    public List<Reservatie> getAllReservaties() {
        return reservatieService.findAllReservaties();
    }

    // GET /reservaties/{id}
    @Operation(summary = "Reservatie ophalen op basis van de id")
    @GetMapping("/{id}")
    public ResponseEntity<Reservatie> getReservatie(@PathVariable Long id) {
        return reservatieService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /reservaties
    @Operation(summary = "reservatie aanmaken")
    @PostMapping
    public Reservatie createReservatie(@RequestBody Reservatie reservatie) {
        return reservatieService.saveReservatie(reservatie);
    }

    // PUT /reservaties/{id}
    @Operation(summary = "Reservatie aanpassen op basis van id")
    @PutMapping("/{id}")
    public ResponseEntity<Reservatie> updateReservatie(@PathVariable Long id, @RequestBody Reservatie nieuweData) {
        return reservatieService.findById(id)
                .map(bestaande -> {
                    bestaande.setLokaal(nieuweData.getLokaal());
                    bestaande.setGebruiker(nieuweData.getGebruiker());
                    bestaande.setStartTijd(nieuweData.getStartTijd());
                    bestaande.setEindTijd(nieuweData.getEindTijd());
                    return ResponseEntity.ok(reservatieService.saveReservatie(bestaande));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /reservaties/{id}
    @Operation(summary = "Reservatie verwijderen op basis van id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservatie(@PathVariable Long id) {
        reservatieService.deleteReservatie(id);
        return ResponseEntity.noContent().build();
    }

    // EXTRA: GET /reservaties/lokaal/{lokaalId}
    @Operation(summary = "Reservaties per lokaal op basis van lokaalid")
    @GetMapping("/lokaal/{lokaalId}")
    public List<Reservatie> getByLokaal(@PathVariable Long lokaalId) {
        return reservatieService.findByLokaal(lokaalId);
    }

    // EXTRA: GET /reservaties/gebruiker/{gebruikerId}
    @Operation(summary = "Reservaties per gebruiker op basis van gebruikerid")
    @GetMapping("/gebruiker/{gebruikerId}")
    public List<Reservatie> getByGebruiker(@PathVariable Long gebruikerId) {
        return reservatieService.findByGebruiker(gebruikerId);
    }
}

