package no.hvl.dat250.voting.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    public User createUser(User user) {
        // error handling if user already exists
        em.persist(user);
        return user;
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    public User findUserById(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(User.class, id);
    }

    public User findUserByUserName(String username) {
        if (username == null) {
            return null;
        }
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    public void deleteUser(User user) {
        em.remove(user);
    }

    public User updateUser(User user) {
        em.merge(user);
        return user;
    }

    public List<Poll> getPollsByUser(Long userId) {
        return em.createQuery("SELECT p FROM Poll p WHERE p.creator.id = :userId", Poll.class)
                .setParameter("userId", userId)
                .getResultList();
    }


}
