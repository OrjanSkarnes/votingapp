package no.hvl.dat250.voting.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;

import java.util.List;

@AllArgsConstructor
public class UserDao {

    @PersistenceContext(unitName = "votingapp")
    private EntityManager em;

    public User createUser(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        return user;
    }

    public User findUserById(Long id) {
        return em.find(User.class, id);
    }

    public void deleteUser(User user) {
        em.getTransaction().begin();
        em.remove(user);
        em.getTransaction().commit();
    }

    public User updateUser(User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
        return user;
    }

    public List<Poll> getPollsByUser(Long userId) {
        return em.createQuery("SELECT p FROM Poll p WHERE p.creator.id = :userId", Poll.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
