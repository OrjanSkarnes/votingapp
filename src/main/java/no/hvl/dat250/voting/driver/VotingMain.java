package no.hvl.dat250.voting.driver;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.Roles;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.Vote;

import java.util.Arrays;

public class VotingMain {

    static final String PERSISTENCE_UNIT_NAME = "voting";

    public static void main(String[] args) {
        try (EntityManagerFactory factory = Persistence.createEntityManagerFactory(
                PERSISTENCE_UNIT_NAME);
             EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();
            populateDatabase(em);
            em.getTransaction().commit();
        }
    }

    private static void populateDatabase(EntityManager em) {
        // TODO: Set up test data
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setRole(Roles.USER);

        em.persist(user);


        Poll poll = new Poll();
        poll.setQuestion("test question");
        poll.setPrivateAccess(true);
        poll.setDescription("Description");
        poll.setActive(true);
        poll.setCreator(user);
        
        em.persist(poll);

        User voter = new User();
        voter.setUsername("Ola");
        voter.setPassword("1234");
        voter.setRole(Roles.USER);

        Vote vote = new Vote();
        vote.setUser(voter);
        vote.setPoll(poll);
        vote.setChoice(false);


        poll.setVotes(Arrays.asList(vote));
        poll.setUsers(Arrays.asList(voter));
        voter.setParticipatedPolls(Arrays.asList(poll));

        em.persist(voter);
        em.persist(vote);
    }
}