package be.ucll.campusapp.service;

import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.repository.LokaalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Dit maakt van deze klasse een Spring service component
public class LokaalService {

    private final LokaalRepository lokaalRepository;

    // Constructor-based dependency injection (autowiring)
    public LokaalService(LokaalRepository lokaalRepository) {
        this.lokaalRepository = lokaalRepository;
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
}

