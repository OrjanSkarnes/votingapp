package no.hvl.dat250.voting.driver;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import static org.hibernate.cfg.AvailableSettings.PERSISTENCE_UNIT_NAME;

@SpringBootApplication
@EnableJpaRepositories
public class VotingMain {
    static final String PERSISTENCE_UNIT_NAME = "voting";

    public static void main(String[] args) {
        try (EntityManagerFactory factory = Persistence.createEntityManagerFactory(
                PERSISTENCE_UNIT_NAME);
             EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();
            em.getTransaction().commit();
        }
        SpringApplication.run(VotingMain.class, args);
    }
}