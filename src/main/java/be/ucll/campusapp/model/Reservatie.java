package be.ucll.campusapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservatie")
public class Reservatie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Starttijd is verplicht")
    @Future(message = "Starttijd moet in de toekomst liggen")
    private LocalDateTime startTijd;

    @NotNull(message = "Eindtijd is verplicht")
    @Future(message = "Eindtijd moet in de toekomst liggen")
    private LocalDateTime eindTijd;

    private String commentaar;

    private int aantalPersonen;

    @ManyToMany
    @JoinTable(
            name = "reservatie_lokaal",
            joinColumns = @JoinColumn(name = "reservatie_id"),
            inverseJoinColumns = @JoinColumn(name = "lokaal_id")
    )
    private Set<Lokaal> lokalen = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "gebruiker_id")
    private User gebruiker;

    // Constructors
    public Reservatie() {}

    public Reservatie(LocalDateTime startTijd, LocalDateTime eindTijd, String commentaar, int aantalPersonen) {
        this.startTijd = startTijd;
        this.eindTijd = eindTijd;
        this.commentaar = commentaar;
        this.aantalPersonen = aantalPersonen;
    }

    // Getters en setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getStartTijd() { return startTijd; }
    public void setStartTijd(LocalDateTime startTijd) { this.startTijd = startTijd; }

    public LocalDateTime getEindTijd() { return eindTijd; }
    public void setEindTijd(LocalDateTime eindTijd) { this.eindTijd = eindTijd; }

    public String getCommentaar() { return commentaar; }
    public void setCommentaar(String commentaar) { this.commentaar = commentaar; }

    public int getAantalPersonen() { return aantalPersonen; }
    public void setAantalPersonen(int aantalPersonen) { this.aantalPersonen = aantalPersonen; }

    public Set<Lokaal> getLokalen() { return lokalen; }
    public void setLokalen(Set<Lokaal> lokalen) { this.lokalen = lokalen; }

    public User getGebruiker() { return gebruiker; }
    public void setGebruiker(User gebruiker) { this.gebruiker = gebruiker; }
}
