package be.ucll.campusapp.repository;

import be.ucll.campusapp.model.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusRepository extends JpaRepository<Campus, String> {
    boolean existsByNaam(String naam);
}
