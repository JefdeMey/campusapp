package be.ucll.campusapp.controller;

import be.ucll.campusapp.dto.CampusCreateDTO;
import be.ucll.campusapp.dto.CampusDTO;
import be.ucll.campusapp.dto.LokaalDTO;
import be.ucll.campusapp.dto.ReservatieDTO;
import be.ucll.campusapp.exception.EntityNotFoundException;
import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.service.CampusService;
import be.ucll.campusapp.service.ReservatieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Campussen", description = "Beheer van campussen")
@RestController
@RequestMapping("/campussen")
public class CampusController {

    private final CampusService campusService;
    private final ReservatieService reservatieService;


    public CampusController(CampusService campusService, ReservatieService reservatieService) {
        this.campusService = campusService;
        this.reservatieService = reservatieService;
    }

    @Operation(summary = "Toon alle campussen")
    @GetMapping
    public List<CampusDTO> getAllCampussen() {
        return campusService.findAllCampussen().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Toon één campus")
    @GetMapping("/{naam}")
    public ResponseEntity<CampusDTO> getCampusByNaam(@PathVariable String naam) {
        Optional<Campus> campus = campusService.findCampusByNaam(naam);

        return campus.map(value -> ResponseEntity.ok(mapToDTO(value)))
                .orElseThrow(() -> new EntityNotFoundException("Campus '" + naam + "' werd niet gevonden."));
    }
    @Operation(summary = "Toon één lokaal binnen een campus")
    @GetMapping("/{campusNaam}/rooms/{roomId}")
    public ResponseEntity<LokaalDTO> getLokaalBinnenCampus(
            @PathVariable String campusNaam,
            @PathVariable Long roomId
    ) {
        Lokaal lokaal = campusService.getLokaalBinnenCampus(campusNaam, roomId);
        return ResponseEntity.ok(mapToLokaalDTO(lokaal));
    }

    @Operation(summary = "Creëer een campus")
    @PostMapping
    public ResponseEntity<CampusDTO> addCampus(@Valid @RequestBody CampusCreateDTO dto) {
        Campus campus = campusService.create(dto);
        return new ResponseEntity<>(mapToDTO(campus), HttpStatus.CREATED);
    }

    @Operation(summary = "Verwijder een campus op basis van een naam")
    @DeleteMapping("/{naam}")
    public ResponseEntity<Void> deleteCampus(@PathVariable String naam) {
        campusService.deleteCampus(naam);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Pas een campus aan op basis van naam")
    @PutMapping("/{naam}")
    public ResponseEntity<CampusDTO> updateCampus(@PathVariable String naam, @Valid @RequestBody CampusCreateDTO dto) {
        try {
            Campus updated = campusService.updateCampus(naam, dto);
            return ResponseEntity.ok(mapToDTO(updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Alle reservaties ophalen voor een lokaal binnen een campus")
    @GetMapping("/{campusNaam}/rooms/{roomId}/reservaties")
    public ResponseEntity<List<ReservatieDTO>> getReservatiesForLokaal(
            @PathVariable String campusNaam,
            @PathVariable Long roomId
    ) {
        List<ReservatieDTO> reservaties = reservatieService.findByCampusAndLokaalId(campusNaam, roomId);
        return ResponseEntity.ok(reservaties);
    }


    // Mapping van Campus naar DTO
    private CampusDTO mapToDTO(Campus campus) {
        CampusDTO dto = new CampusDTO();
        dto.setNaam(campus.getNaam());
        dto.setAdres(campus.getAdres());
        dto.setAantalParkeerplaatsen(campus.getAantalParkeerplaatsen());
        dto.setAantalLokalen(campus.getLokalen() != null ? campus.getLokalen().size() : 0);
        return dto;
    }
    private LokaalDTO mapToLokaalDTO(Lokaal lokaal) {
        LokaalDTO dto = new LokaalDTO();
        dto.setId(lokaal.getId());
        dto.setNaam(lokaal.getNaam());
        dto.setType(lokaal.getType());
        dto.setAantalPersonen(lokaal.getAantalPersonen());
        dto.setVoornaam(lokaal.getVoornaam());
        dto.setAchternaam(lokaal.getAchternaam());
        dto.setVerdieping(lokaal.getVerdieping());
        return dto;
    }
}
