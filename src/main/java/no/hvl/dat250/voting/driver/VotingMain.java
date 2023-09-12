package no.hvl.dat250.voting.driver;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.Roles;
import no.hvl.dat250.voting.User;

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

        System.out.println(user.getCreatedPolls().size());
        System.out.println(user.toString());
        System.out.println(poll.toString());
        em.persist(poll);
    }
}