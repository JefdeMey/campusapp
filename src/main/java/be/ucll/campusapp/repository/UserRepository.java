package be.ucll.campusapp.repository;

import be.ucll.campusapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring maakt automatisch CRUD-methoden aan
}

