package be.ucll.campusapp.service;

import be.ucll.campusapp.dto.CampusCreateDTO;
import be.ucll.campusapp.dto.LokaalDTO;
import be.ucll.campusapp.exception.EntityNotFoundException;
import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.repository.CampusRepository;
import be.ucll.campusapp.repository.LokaalRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CampusService {

    private final CampusRepository campusRepository;
    private final LokaalRepository lokaalRepository;

    public CampusService(CampusRepository campusRepository, LokaalRepository lokaalRepository) {
        this.campusRepository = campusRepository;
        this.lokaalRepository = lokaalRepository;
    }

    public List<Campus> findAllCampussen() {
        return campusRepository.findAll();
    }

    public Optional<Campus> findCampusByNaam(String naam) {
        return campusRepository.findById(naam);
    }

    public Campus create(CampusCreateDTO dto) {
        if (campusRepository.existsByNaam(dto.getNaam())) {
            throw new IllegalArgumentException("Campus met deze naam bestaat al.");
        }

        Campus campus = new Campus();
        campus.setNaam(dto.getNaam());
        campus.setAdres(dto.getAdres());
        campus.setAantalParkeerplaatsen(dto.getAantalParkeerPlaatsen());
        return campusRepository.save(campus);
    }

    public boolean deleteCampus(String naam) {
        Optional<Campus> campus = campusRepository.findById(naam);
        if (campus.isEmpty()) {
            return false;
        }
        campusRepository.delete(campus.get());
        return true;
    }

    public Campus updateCampus(String naam, CampusCreateDTO dto) {
        Campus existing = campusRepository.findById(naam)
                .orElseThrow(() -> new EntityNotFoundException("Campus '" + naam + "' werd niet gevonden."));

        existing.setAdres(dto.getAdres());
        existing.setAantalParkeerplaatsen(dto.getAantalParkeerPlaatsen());

        return campusRepository.save(existing);
    }

    public Campus getCampusByNaamOrThrow(String naam) {
        return campusRepository.findById(naam)
                .orElseThrow(() -> new EntityNotFoundException("Campus '" + naam + "' werd niet gevonden."));
    }

    public Lokaal getLokaalBinnenCampus(String campusNaam, Long lokaalId) {
        Lokaal lokaal = lokaalRepository.findById(lokaalId)
                .orElseThrow(() -> new EntityNotFoundException("Lokaal met ID " + lokaalId + " niet gevonden."));

        if (!lokaal.getCampus().getNaam().equalsIgnoreCase(campusNaam)) {
            throw new IllegalArgumentException("Lokaal hoort niet bij campus " + campusNaam);
        }

        return lokaal;
    }

    public List<LokaalDTO> findLokalenMetFilters(String campusNaam, LocalDateTime availableFrom, LocalDateTime availableUntil, Integer minNumberOfSeats) {
        List<Lokaal> alleLokalen = lokaalRepository.findByCampus_Naam(campusNaam);

        return alleLokalen.stream()
                .filter(lokaal -> beschikbaarVanaf(lokaal, availableFrom))
                .filter(lokaal -> beschikbaarTot(lokaal, availableUntil))
                .filter(lokaal -> heeftMinAantalZitplaatsen(lokaal, minNumberOfSeats))
                .map(this::mapToDTO)
                .toList();
    }

    private boolean beschikbaarVanaf(Lokaal lokaal, LocalDateTime tijdstip) {
        if (tijdstip == null) return true;
        return lokaal.getReservaties().stream()
                .noneMatch(r -> !r.getEindTijd().isBefore(tijdstip));
    }

    private boolean beschikbaarTot(Lokaal lokaal, LocalDateTime tijdstip) {
        if (tijdstip == null) return true;
        return lokaal.getReservaties().stream()
                .noneMatch(r -> !r.getStartTijd().isAfter(tijdstip));
    }

    private boolean heeftMinAantalZitplaatsen(Lokaal lokaal, Integer minAantal) {
        if (minAantal == null) return true;
        return lokaal.getAantalPersonen() >= minAantal;
    }

    private LokaalDTO mapToDTO(Lokaal lokaal) {
        LokaalDTO dto = new LokaalDTO();
        dto.setId(lokaal.getId());
        dto.setNaam(lokaal.getNaam());
        dto.setType(lokaal.getType());
        dto.setAantalPersonen(lokaal.getAantalPersonen());
        dto.setVoornaam(lokaal.getVoornaam());
        dto.setAchternaam(lokaal.getAchternaam());
        dto.setVerdieping(lokaal.getVerdieping());
        if (lokaal.getCampus() != null) {
            dto.setCampusNaam(lokaal.getCampus().getNaam());
        }
        return dto;
    }

}
