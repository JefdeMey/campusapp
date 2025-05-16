package be.ucll.campusapp.controller;

import be.ucll.campusapp.dto.CampusUpdateDTO;
import be.ucll.campusapp.exception.EntityNotFoundException;
import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.service.CampusService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Optional;

@Tag(name = "Campussen", description = "Beheer van campussen")
@RestController // Maakt van deze klasse een REST-controller
@RequestMapping("/campussen") // Basis-URL voor deze controller
public class CampusController {

    private final CampusService campusService;

    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }

    // GET /campussen
    @Operation(summary = "Toon alle campussen")
    @GetMapping
    public List<Campus> getAllCampussen() {
        return campusService.findAllCampussen();
    }

    // GET /campussen/{naam}
    @Operation(summary = "Toon één campus")
    @GetMapping("/{naam}")
    public ResponseEntity<Campus> getCampusByNaam(@PathVariable String naam) {
        Optional<Campus> campus = campusService.findCampusByNaam(naam);

        if (campus.isEmpty()) {
            throw new EntityNotFoundException("Campus '" + naam + "' werd niet gevonden.");
        }

        return ResponseEntity.ok(campus.get());
    }

    // POST /campussen
    @Operation(summary = "Creëer een campus")
    @PostMapping
    public ResponseEntity<Campus> addCampus(@Valid @RequestBody Campus campus) {
        Campus saved = campusService.saveCampus(campus);
        return ResponseEntity.ok(saved);
    }

    // DELETE /campussen/{naam}
    @Operation(summary = "Verwijder een campus op basis van een naam")
    @DeleteMapping("/{naam}")
    public ResponseEntity<Void> deleteCampus(@PathVariable String naam) {
        campusService.deleteCampus(naam);
        return ResponseEntity.noContent().build();
    }
    // PUT /campussen/{naam}
    @Operation(summary = "Pas een campus aan op basis van naam")
    @PutMapping("/{naam}")
    public ResponseEntity<Campus> updateCampus(@PathVariable String naam, @Valid @RequestBody CampusUpdateDTO dto) {
        return campusService.findCampusByNaam(naam)
                .map(existingCampus -> {
                    existingCampus.setAdres(dto.getAdres());
                    existingCampus.setAantalParkeerplaatsen(dto.getAantalParkeerplaatsen());
                    // voeg hier eventueel meer velden toe als je dat later hebt
                    Campus saved = campusService.saveCampus(existingCampus);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

