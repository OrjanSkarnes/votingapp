package no.hvl.dat250.voting;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Users") 
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
    @JsonManagedReference("user-poll")
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Poll> createdPolls = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    private List<Poll> participatedPolls = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Vote> votes = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    private List<Group> groups = new ArrayList<>();
}

