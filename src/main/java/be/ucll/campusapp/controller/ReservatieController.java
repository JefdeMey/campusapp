package be.ucll.campusapp.controller;

import be.ucll.campusapp.dto.ReservatieCreateDTO;
import be.ucll.campusapp.dto.ReservatieDTO;
import be.ucll.campusapp.dto.ReservatieUpdateDTO;
import be.ucll.campusapp.service.ReservatieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @Operation(summary = "Alle reservaties ophalen")
    @GetMapping
    public List<ReservatieDTO> getAll() {
        return reservatieService.getAll();
    }

    @Operation(summary = "Eén reservatie ophalen op basis van ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservatieDTO> getById(@PathVariable Long id) {
        return reservatieService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Nieuwe reservatie aanmaken")
    @PostMapping
    public ResponseEntity<ReservatieDTO> create(@Valid @RequestBody ReservatieCreateDTO dto) {
        ReservatieDTO created = reservatieService.create(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Reservatie bijwerken op basis van ID")
    @PutMapping("/{id}")
    public ResponseEntity<ReservatieDTO> update(@PathVariable Long id, @Valid @RequestBody ReservatieUpdateDTO dto) {
        return reservatieService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Reservatie verwijderen op basis van ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservatieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
