package be.ucll.campusapp.repository;

import be.ucll.campusapp.model.Lokaal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LokaalRepository extends JpaRepository<Lokaal, Long> {
    // hier kunnen later filters komen zoals findByCampus_Naam(...)
    Optional<Lokaal> findByCampus_NaamAndNaam(String campusNaam, String lokaalNaam);
}
