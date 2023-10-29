    package no.hvl.dat250.voting;

    import jakarta.persistence.*;
    import lombok.*;

    import java.util.ArrayList;
    import java.util.List;

    @Getter
    @Setter
    @Entity
    @NoArgsConstructor
    @Table(name = "Users") // "User" is a reserved keyword in SQL
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true) // Username must be unique
        private String username;

        private String password;

        @Enumerated(EnumType.STRING)
        private Roles role;

        // Cascade all ensures that if we delete a user, all polls created by that user will also be deleted
        @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
        private List<Poll> createdPolls = new ArrayList<>();

        @ManyToMany(mappedBy = "users")
        private List<Poll> participatedPolls = new ArrayList<>();

        @OneToMany(mappedBy = "user")
        private List<Vote> votes = new ArrayList<>();

        @ManyToMany(mappedBy = "members")
        private List<Group> groups = new ArrayList<>();
    }

