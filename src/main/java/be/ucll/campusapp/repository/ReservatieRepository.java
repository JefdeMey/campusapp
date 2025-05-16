package be.ucll.campusapp.repository;

import be.ucll.campusapp.model.Reservatie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservatieRepository extends JpaRepository<Reservatie, Long> {

    // Optioneel: alle reservaties voor een specifiek lokaal
    List<Reservatie> findByLokaal_Id(Long lokaalId);

    // Optioneel: alle reservaties voor een specifieke gebruiker
    List<Reservatie> findByGebruiker_Id(Long gebruikerId);

    // Optioneel: alle reservaties op een bepaald moment (bijv. voor overlapcontrole)
    List<Reservatie> findByLokaal_IdAndStartTijdLessThanEqualAndEindTijdGreaterThanEqual(
            Long lokaalId, LocalDateTime eindTijd, LocalDateTime startTijd
    );
}

