package be.ucll.campusapp.bootstrap;

import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.model.Reservatie;
import be.ucll.campusapp.model.User;
import be.ucll.campusapp.repository.CampusRepository;
import be.ucll.campusapp.repository.LokaalRepository;
import be.ucll.campusapp.repository.ReservatieRepository;
import be.ucll.campusapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CampusRepository campusRepository;
    private final LokaalRepository lokaalRepository;
    private final UserRepository userRepository;
    private final ReservatieRepository reservatieRepository;

    public DataInitializer(CampusRepository campusRepository,
                           LokaalRepository lokaalRepository,
                           UserRepository userRepository,
                           ReservatieRepository reservatieRepository) {
        this.campusRepository = campusRepository;
        this.lokaalRepository = lokaalRepository;
        this.userRepository = userRepository;
        this.reservatieRepository = reservatieRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println(">>>> DataInitializer wordt uitgevoerd");
        if (campusRepository.count() == 0) {
            Campus campus = new Campus("LEUVEN", "Naamsestraat 1", 120);
            campusRepository.save(campus);

            Lokaal l1 = new Lokaal("1.01", "Leslokaal", 30, "Jan", "Peeters", 1, campus);
            Lokaal l2 = new Lokaal("1.02", "PC-lokaal", 20, "Lisa", "Vermeulen", 1, campus);
            lokaalRepository.save(l1);
            lokaalRepository.save(l2);

            User u1 = new User("Alice", "Maes");
            User u2 = new User("Bram", "Dewitte");
            userRepository.save(u1);
            userRepository.save(u2);

            Reservatie r1 = new Reservatie();
            r1.setStartTijd(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
            r1.setEindTijd(LocalDateTime.now().plusDays(1).withHour(12).withMinute(0));
            r1.setCommentaar("Introductie Java");
            r1.setAantalPersonen(25);
            r1.setGebruiker(u1);
            r1.setLokalen(Set.of(l1, l2));
            reservatieRepository.save(r1);

            System.out.println("Testdata toegevoegd: campus, lokalen, gebruikers, reservatie");
        }
    }
}
