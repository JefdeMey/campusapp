package be.ucll.campusapp.repository;

import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.model.Reservatie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservatieRepository extends JpaRepository<Reservatie, Long> {

    List<Reservatie> findByLokalen_Id(Long lokaalId);

    List<Reservatie> findByLokalen_IdAndStartTijdLessThanEqualAndEindTijdGreaterThanEqual(
            Long lokaalId, LocalDateTime eind, LocalDateTime start
    );
    List<Reservatie> findByLokalenContainingOrderByStartTijdAsc(Lokaal lokaal);
    List<Reservatie> findByGebruiker_IdOrderByStartTijdAsc(Long gebruikerId);

}

