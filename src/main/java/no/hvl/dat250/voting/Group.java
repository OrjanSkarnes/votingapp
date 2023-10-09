package no.hvl.dat250.voting;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

 // I think this might be complicating things a bit more than necessary in this task
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user_groups") // "Group" is a reserved keyword in SQL
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private String name;

    @ManyToMany
    private List<User> members = new ArrayList<>();

    @ManyToMany(mappedBy = "groups")
    private List<Poll> polls = new ArrayList<>();
}
