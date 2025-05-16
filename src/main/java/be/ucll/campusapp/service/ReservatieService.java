package be.ucll.campusapp.service;

import be.ucll.campusapp.model.Reservatie;
import be.ucll.campusapp.repository.ReservatieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservatieService {

    private final ReservatieRepository reservatieRepository;

    public ReservatieService(ReservatieRepository reservatieRepository) {
        this.reservatieRepository = reservatieRepository;
    }

    // Alle reservaties
    public List<Reservatie> findAllReservaties() {
        return reservatieRepository.findAll();
    }

    // Één reservatie
    public Optional<Reservatie> findById(Long id) {
        return reservatieRepository.findById(id);
    }

    // Opslaan
    public Reservatie saveReservatie(Reservatie reservatie) {
        return reservatieRepository.save(reservatie);
    }

    // Verwijderen
    public void deleteReservatie(Long id) {
        reservatieRepository.deleteById(id);
    }

    // Extra: reservaties per lokaal
    public List<Reservatie> findByLokaal(Long lokaalId) {
        return reservatieRepository.findByLokaal_Id(lokaalId);
    }

    // Extra: reservaties per gebruiker
    public List<Reservatie> findByGebruiker(Long gebruikerId) {
        return reservatieRepository.findByGebruiker_Id(gebruikerId);
    }
}

