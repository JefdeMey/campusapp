package be.ucll.campusapp.service;

import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.repository.CampusRepository;
import be.ucll.campusapp.repository.LokaalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service // Dit maakt van deze klasse een Spring service component
public class LokaalService {

    private final LokaalRepository lokaalRepository;
    private final CampusRepository campusRepository;

    // Constructor-based dependency injection (autowiring)
    public LokaalService(LokaalRepository lokaalRepository, CampusRepository campusRepository) {

        this.lokaalRepository = lokaalRepository;
        this.campusRepository = campusRepository;
    }

    // Geef alle lokalen terug
    public List<Lokaal> findAllLokalen() {
        return lokaalRepository.findAll();
    }

    // Geef één lokaal op ID
    // alternatieve versie van de optional (impliciet) --> gemakkelijker te gebruiken in de controller
    public Optional<Lokaal> findLokaalById(Long id) {
        return lokaalRepository.findById(id);
    }

    // Sla een lokaal op (insert of update)
    public Lokaal saveLokaal(Lokaal lokaal) {
        String campusNaam = lokaal.getCampus().getNaam();
        if(!campusRepository.existsById(campusNaam)){
            throw new IllegalArgumentException("De opgegegeven campus " + campusNaam + " bestaat niet!");
        }
        boolean bestaatAl = lokaalRepository
                .findByCampus_NaamAndNaam(lokaal.getCampus().getNaam(), lokaal.getNaam())
                .isPresent();

        if (bestaatAl && (lokaal.getId() == null)) {
            throw new IllegalArgumentException("Lokaal met deze naam bestaat al in deze campus.");
        }

        return lokaalRepository.save(lokaal);
    }

    // Verwijder een lokaal
    public void deleteLokaal(Long id) {
        lokaalRepository.deleteById(id);
    }


    public Lokaal getLokaalByIdOrThrow(Long id) {
        return lokaalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lokaal met ID " + id + " werd niet gevonden."));
    }
    //extra filters
    public List<Lokaal> findLokalenByCampusNaam(String campusNaam) {
        return lokaalRepository.findByCampus_Naam(campusNaam);
    }
    public List<Lokaal> findLokalenByVerdiepingAndCampus(int verdieping, String campusNaam) {
        return lokaalRepository.findByVerdiepingAndCampus_Naam(verdieping, campusNaam);
    }
}

