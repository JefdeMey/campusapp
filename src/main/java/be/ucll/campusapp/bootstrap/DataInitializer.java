package be.ucll.campusapp.bootstrap;

import be.ucll.campusapp.model.Campus;
import be.ucll.campusapp.model.Lokaal;
import be.ucll.campusapp.repository.CampusRepository;
import be.ucll.campusapp.repository.LokaalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
//tijdelijke of eenmalige opstarttaken in de bootstrap. We gaan wat data in de database steken zodat we beter kunnen testen.
@Component // zorgt ervoor dat Spring deze klasse automatisch uitvoert
public class DataInitializer implements CommandLineRunner {

    private final CampusRepository campusRepository;
    private final LokaalRepository lokaalRepository;

    public DataInitializer(CampusRepository campusRepository, LokaalRepository lokaalRepository) {
        this.campusRepository = campusRepository;
        this.lokaalRepository = lokaalRepository;
    }

    @Override
    public void run(String... args) {
        if(campusRepository.count() == 0){
            // Een campus toevoegen
            Campus campus = new Campus("LEUVEN", "Naamsestraat 1", 120);
            campusRepository.save(campus);

            // Enkele lokalen aanmaken voor die campus
            Lokaal l1 = new Lokaal("1.01", "Leslokaal", 30, "Jan", "Peeters", 1, campus);
            Lokaal l2 = new Lokaal("1.02", "PC-lokaal", 20, "Lisa", "Vermeulen", 1, campus);

            lokaalRepository.save(l1);
            lokaalRepository.save(l2);

            // Print in console voor bevestiging
            System.out.println("Testdata toegevoegd: campus + 2 lokalen");
        }

    }
}

