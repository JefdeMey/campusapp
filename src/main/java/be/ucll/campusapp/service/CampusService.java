package be.ucll.campusapp.service;

import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.repository.CampusRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;

@Service // Zorgt ervoor dat Spring deze klasse herkent als service-component
public class CampusService {

    private final CampusRepository campusRepository;
// dependency injection via de ctor
    public CampusService(CampusRepository campusRepository) {
        this.campusRepository = campusRepository;
    }

    // Geef alle campussen terug
    public List<Campus> findAllCampussen() {
        return campusRepository.findAll();
    }

    // Geef één campus op naam
    //
    public Optional<Campus> findCampusByNaam(String naam) {
        return campusRepository.findById(naam);
    }

    // Sla een campus op (insert of update)
    public Campus saveCampus(Campus campus) {
        return campusRepository.save(campus);
    }

    // Verwijder een campus
    public void deleteCampus(String naam) {
        campusRepository.deleteById(naam);
    }

}
