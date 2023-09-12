package no.hvl.dat250.voting;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    private Boolean choice;

    private LocalDateTime timestamp;

    @ManyToOne
    private User user;

    @ManyToOne
    private Poll poll;
}
