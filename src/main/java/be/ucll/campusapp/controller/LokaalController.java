package be.ucll.campusapp.controller;

import be.ucll.campusapp.dto.LokaalDTO;
import be.ucll.campusapp.dto.LokaalCreateDTO;
import be.ucll.campusapp.exception.EntityNotFoundException;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.repository.LokaalRepository;
import be.ucll.campusapp.service.LokaalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Lokalen", description = "Beheer van lokalen")
@RestController
@RequestMapping("/lokalen")
public class LokaalController {

    private final LokaalService lokaalService;
    private final LokaalRepository lokaalRepository;

    public LokaalController(LokaalService lokaalService, LokaalRepository lokaalRepository) {
        this.lokaalService = lokaalService;
        this.lokaalRepository = lokaalRepository;
    }

    @Operation(summary = "Toon alle lokalen")
    @GetMapping
    public List<LokaalDTO> getAllLokalen() {
        return lokaalService.findAllLokalen()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Toon één lokaal")
    @GetMapping("/{id}")
    public ResponseEntity<LokaalDTO> getLokaalById(@PathVariable Long id) {
        Optional<Lokaal> lokaal = lokaalService.findLokaalById(id);
        return lokaal.map(value -> ResponseEntity.ok(mapToDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Toon lokaalid op naam")
    @GetMapping("/naam/{naam}")
    public ResponseEntity<Long> getIdByNaam(@PathVariable String naam) {
        Optional<Lokaal> lokaal = lokaalRepository.findByNaam(naam);
        if (lokaal.isPresent()) {
            return ResponseEntity.ok(lokaal.get().getId());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Creëer een lokaal binnen een campus")
    @PostMapping("/campus/{campusId}")
    public ResponseEntity<LokaalDTO> createLokaal(@PathVariable String campusId, @Valid @RequestBody LokaalCreateDTO dto) {
        Lokaal lokaal = lokaalService.create(campusId, dto);
        return new ResponseEntity<>(mapToDTO(lokaal), HttpStatus.CREATED);
    }
    @Operation(summary = "Pas een lokaal aan op basis van ID")
    @PutMapping("/{id}")
    public ResponseEntity<LokaalDTO> updateLokaal(@PathVariable Long id, @Valid @RequestBody LokaalCreateDTO dto) {
        try {
            Lokaal updated = lokaalService.updateLokaal(id, dto);
            return ResponseEntity.ok(mapToDTO(updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Verwijder een lokaal op basis van ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLokaal(@PathVariable Long id) {
        lokaalService.deleteLokaal(id);
        return ResponseEntity.noContent().build();
    }

    // Mapping van entiteit naar DTO
    private LokaalDTO mapToDTO(Lokaal lokaal) {
        LokaalDTO dto = new LokaalDTO();
        dto.setId(lokaal.getId());
        dto.setNaam(lokaal.getNaam());
        dto.setType(lokaal.getType());
        dto.setAantalPersonen(lokaal.getAantalPersonen());
        dto.setVoornaam(lokaal.getVoornaam());
        dto.setAchternaam(lokaal.getAchternaam());
        dto.setVerdieping(lokaal.getVerdieping());
        dto.setCampusNaam(lokaal.getCampus().getNaam());
        return dto;
    }
}


