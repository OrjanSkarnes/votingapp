package no.hvl.dat250.voting;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class VotingMain implements CommandLineRunner{

    @Autowired EntityManagerFactory factory;
    public static void main(String[] args) {
        SpringApplication.run(VotingMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.getTransaction().commit();
    }


}