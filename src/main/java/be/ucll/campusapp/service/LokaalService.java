package be.ucll.campusapp.service;

import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.repository.LokaalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Lokaal findLokaalById(Long id) {
        return lokaalRepository.findById(id).orElse(null);
    }

    // Sla een lokaal op (insert of update)
    public Lokaal saveLokaal(Lokaal lokaal) {
        return lokaalRepository.save(lokaal);
    }

    // Verwijder een lokaal
    public void deleteLokaal(Long id) {
        lokaalRepository.deleteById(id);
    }
}

