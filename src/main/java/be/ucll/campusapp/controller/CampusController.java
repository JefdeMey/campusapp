package be.ucll.campusapp.controller;

import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.service.CampusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Maakt van deze klasse een REST-controller
@RequestMapping("/campussen") // Basis-URL voor deze controller
public class CampusController {

    private final CampusService campusService;

    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }

    // GET /campussen
    @GetMapping
    public List<Campus> getAllCampussen() {
        return campusService.findAllCampussen();
    }

    // GET /campussen/{naam}
    @GetMapping("/{naam}")
    public ResponseEntity<Campus> getCampusByNaam(@PathVariable String naam) {
        return campusService.findCampusByNaam(naam)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /campussen
    @PostMapping
    public Campus addCampus(@RequestBody Campus campus) {
        return campusService.saveCampus(campus);
    }

    // DELETE /campussen/{naam}
    @DeleteMapping("/{naam}")
    public ResponseEntity<Void> deleteCampus(@PathVariable String naam) {
        campusService.deleteCampus(naam);
        return ResponseEntity.noContent().build();
    }
    // PUT /campussen/{naam}
    @PutMapping("/{naam}")
    public ResponseEntity<Campus> updateCampus(@PathVariable String naam, @RequestBody Campus updatedCampus) {
        return campusService.findCampusByNaam(naam)
                .map(existingCampus -> {
                    existingCampus.setAdres(updatedCampus.getAdres());
                    existingCampus.setAantalParkeerplaatsen(updatedCampus.getAantalParkeerplaatsen());
                    // voeg hier eventueel meer velden toe als je dat later hebt
                    Campus saved = campusService.saveCampus(existingCampus);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

