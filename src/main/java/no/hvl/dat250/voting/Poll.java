package no.hvl.dat250.voting;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Transient
    private int timeLimit;

    private boolean active;

    private boolean privateAccess;

    @JsonManagedReference("poll-vote")
    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();


    @ManyToMany
    private List<Group> groups = new ArrayList<>();

    @ManyToMany
    private List<User> users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
}
