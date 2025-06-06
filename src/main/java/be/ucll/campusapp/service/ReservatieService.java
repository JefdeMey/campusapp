package be.ucll.campusapp.service;

import be.ucll.campusapp.dto.ReservatieCreateDTO;
import be.ucll.campusapp.dto.ReservatieDTO;
import be.ucll.campusapp.dto.ReservatieUpdateDTO;
import be.ucll.campusapp.exception.EntityNotFoundException;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.model.Reservatie;
import be.ucll.campusapp.model.User;
import be.ucll.campusapp.repository.LokaalRepository;
import be.ucll.campusapp.repository.ReservatieRepository;
import be.ucll.campusapp.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservatieService {

    private final ReservatieRepository reservatieRepository;
    private final UserRepository userRepository;
    private final LokaalRepository lokaalRepository;

    public ReservatieService(ReservatieRepository reservatieRepository,
                             UserRepository userRepository,
                             LokaalRepository lokaalRepository) {
        this.reservatieRepository = reservatieRepository;
        this.userRepository = userRepository;
        this.lokaalRepository = lokaalRepository;
    }

    public List<ReservatieDTO> getAll() {
        return reservatieRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReservatieDTO> getById(Long id) {
        return reservatieRepository.findById(id).map(this::mapToDTO);
    }

    public ReservatieDTO create(ReservatieCreateDTO dto) {
        validatePeriode(dto.getStartTijd(), dto.getEindTijd());

        Set<Long> uniekeIds = new HashSet<>(dto.getLokaalIds());
        if (uniekeIds.size() != dto.getLokaalIds().size()) {
            throw new IllegalArgumentException("Een lokaal mag niet meerdere keren gekozen worden voor dezelfde reservatie.");
        }

        for (Long lokaalId : dto.getLokaalIds()) {
            List<Reservatie> overlapping = reservatieRepository
                    .findByLokalen_IdAndStartTijdLessThanEqualAndEindTijdGreaterThanEqual(
                            lokaalId, dto.getEindTijd(), dto.getStartTijd());

            if (!overlapping.isEmpty()) {
                throw new IllegalArgumentException("Lokaal met ID " + lokaalId + " is reeds gereserveerd in deze periode.");
            }
        }
        User gebruiker = userRepository.getReferenceById(dto.getGebruikerId());
//        User gebruiker = userRepository.findById(dto.getGebruikerId())
//                .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

        Set<Lokaal> lokalen = fetchLokalen(dto.getLokaalIds());

        Reservatie reservatie = new Reservatie();
        reservatie.setStartTijd(dto.getStartTijd());
        reservatie.setEindTijd(dto.getEindTijd());
        reservatie.setCommentaar(dto.getCommentaar());
        reservatie.setAantalPersonen(dto.getAantalPersonen());
        System.out.println("➡️ Debug: gebruiker = " + gebruiker.getId() + ", object = " + gebruiker);
        reservatie.setGebruiker(gebruiker);
        reservatie.setLokalen(lokalen);

        int maxCapaciteit = lokalen.stream()
                .mapToInt(Lokaal::getAantalPersonen)
                .sum();

        if (dto.getAantalPersonen() > maxCapaciteit) {
            throw new IllegalArgumentException("Aantal personen (" + dto.getAantalPersonen() +
                    ") overschrijdt de totale capaciteit (" + maxCapaciteit + ") van de gekozen lokalen.");
        }

        return mapToDTO(reservatieRepository.save(reservatie));
    }

    public Optional<ReservatieDTO> update(Long id, ReservatieUpdateDTO dto) {
        return reservatieRepository.findById(id).map(reservatie -> {
            validatePeriode(dto.getStartTijd(), dto.getEindTijd());

            Set<Long> uniekeIds = new HashSet<>(dto.getLokaalIds());
            if (uniekeIds.size() != dto.getLokaalIds().size()) {
                throw new IllegalArgumentException("Een lokaal mag niet meerdere keren gekozen worden voor dezelfde reservatie.");
            }

            Set<Lokaal> lokalen = fetchLokalen(dto.getLokaalIds());

            for (Long lokaalId : dto.getLokaalIds()) {
                List<Reservatie> overlapping = reservatieRepository
                        .findByLokalen_IdAndStartTijdLessThanEqualAndEindTijdGreaterThanEqual(
                                lokaalId, dto.getEindTijd(), dto.getStartTijd());

                boolean overlaptAnders = overlapping.stream()
                        .anyMatch(r -> !r.getId().equals(reservatie.getId())); // sluit huidige reservatie uit

                if (overlaptAnders) {
                    throw new IllegalArgumentException("Lokaal met ID " + lokaalId + " is reeds gereserveerd in deze periode.");
                }
            }

            reservatie.setStartTijd(dto.getStartTijd());
            reservatie.setEindTijd(dto.getEindTijd());
            reservatie.setCommentaar(dto.getCommentaar());
            reservatie.setAantalPersonen(dto.getAantalPersonen());
            int maxCapaciteit = lokalen.stream()
                    .mapToInt(Lokaal::getAantalPersonen)
                    .sum();

            if (dto.getAantalPersonen() > maxCapaciteit) {
                throw new IllegalArgumentException("Aantal personen (" + dto.getAantalPersonen() +
                        ") overschrijdt de totale capaciteit (" + maxCapaciteit + ") van de gekozen lokalen.");
            }

            reservatie.setLokalen(lokalen);
            // Overlapcontrole voor elk lokaal (behalve huidige reservatie zelf)

            return mapToDTO(reservatieRepository.save(reservatie));
        });
    }

    public void delete(Long id) {
        if (!reservatieRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservatie met ID " + id + " bestaat niet.");
        }

        reservatieRepository.deleteById(id);
    }

    private void validatePeriode(LocalDateTime start, LocalDateTime eind) {
        if (!start.isBefore(eind)) {
            throw new IllegalArgumentException("Starttijd moet vóór de eindtijd liggen.");
        }
    }

    private Set<Lokaal> fetchLokalen(List<Long> ids) {
        List<Lokaal> lokalen = lokaalRepository.findAllById(ids);
        if (lokalen.size() != ids.size()) {
            throw new IllegalArgumentException("Eén of meerdere lokalen bestaan niet.");
        }
        return new HashSet<>(lokalen);
    }

    private ReservatieDTO mapToDTO(Reservatie r) {
        ReservatieDTO dto = new ReservatieDTO();
        dto.setId(r.getId());
        dto.setStartTijd(r.getStartTijd());
        dto.setEindTijd(r.getEindTijd());
        dto.setCommentaar(r.getCommentaar());
        dto.setAantalPersonen(r.getAantalPersonen());
        dto.setGebruikerNaam(r.getGebruiker().getVoornaam() + " " + r.getGebruiker().getAchternaam());
        dto.setLokaalNamen(r.getLokalen().stream()
                .map(Lokaal::getNaam)
                .collect(Collectors.toList()));
        return dto;
    }

    public List<ReservatieDTO> findByGebruikerId(Long gebruikerId) {
    return reservatieRepository.findByGebruiker_IdOrderByStartTijdAsc(gebruikerId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
}

    public List<ReservatieDTO> findByLokaalId(Long lokaalId) {
        return reservatieRepository.findByLokalen_Id(lokaalId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservatieDTO> findByCampusAndLokaalId(String campusNaam, Long lokaalId) {
        Lokaal lokaal = lokaalRepository.findById(lokaalId)
                .orElseThrow(() -> new EntityNotFoundException("Lokaal niet gevonden."));

        if (!lokaal.getCampus().getNaam().equalsIgnoreCase(campusNaam)) {
            throw new IllegalArgumentException("Lokaal hoort niet bij campus " + campusNaam);
        }

        List<Reservatie> reservaties = reservatieRepository.findByLokalenContainingOrderByStartTijdAsc(lokaal);
        return reservaties.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public ReservatieDTO voegLokaalToeAanReservatie(Long userId, Long reservatieId, Long lokaalId) {
        Reservatie reservatie = reservatieRepository.findById(reservatieId)
                .orElseThrow(() -> new EntityNotFoundException("Reservatie niet gevonden."));

        if (!reservatie.getGebruiker().getId().equals(userId)) {
            throw new IllegalArgumentException("Reservatie behoort niet toe aan gebruiker " + userId);
        }

        Lokaal lokaal = lokaalRepository.findById(lokaalId)
                .orElseThrow(() -> new EntityNotFoundException("Lokaal niet gevonden."));

        // Check of lokaal al toegevoegd is
        if (reservatie.getLokalen().contains(lokaal)) {
            throw new IllegalArgumentException("Lokaal is al gekoppeld aan deze reservatie.");
        }

        // Check op beschikbaarheid (overlap)
        List<Reservatie> overlapping = reservatieRepository
                .findByLokalen_IdAndStartTijdLessThanEqualAndEindTijdGreaterThanEqual(
                        lokaalId, reservatie.getEindTijd(), reservatie.getStartTijd());

        boolean overlapt = overlapping.stream()
                .anyMatch(r -> !r.getId().equals(reservatie.getId()));

        if (overlapt) {
            throw new IllegalArgumentException("Lokaal is reeds gereserveerd in deze periode.");
        }

        // Alles ok, toevoegen
        reservatie.getLokalen().add(lokaal);
        Reservatie updated = reservatieRepository.save(reservatie);
        return mapToDTO(updated);
    }

}
