package no.hvl.dat250.voting.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import no.hvl.dat250.voting.models.Poll;
import no.hvl.dat250.voting.models.User;
import no.hvl.dat250.voting.models.Vote;

import no.hvl.dat250.voting.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@AllArgsConstructor
@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private LoggerService loggerService;

    public User createUser(User user, Long tempId) {
        // error handling if user already exists
        em.persist(user);
        updateVotesWithTempId(user, tempId);
        return user;
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    public User findUserById(Long id) {
        if (id == null) {
            loggerService.logError("User id is null");
            return null;
        }
        return em.find(User.class, id);
    }

    public List<User> findUsersByIds(List<Long> userIds) {
        if (userIds == null) {
            loggerService.logError("User ids is null");
            return null;
        }
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id IN :userIds", User.class);
        query.setParameter("userIds", userIds);
        return query.getResultList();
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

    public boolean existsByUsername(String username) {
        if (username == null) {
            return false;
        }
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();

        return !users.isEmpty();
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

    public List<Vote> getVotesByUser(Long userId) {
        User user = findUserById(userId);
        if(user != null) {
            return user.getVotes();
        }
        return new ArrayList<>();
    }


    // WHen a user is registered it should map the user id to the anon votes
    public void updateVotesWithTempId(User user, Long tempId) {
        TypedQuery<Vote> query = em.createQuery("SELECT v FROM Vote v WHERE v.tempId = :tempId", Vote.class);
        query.setParameter("tempId", tempId);
        List<Vote> votes = query.getResultList();
        for (Vote vote : votes) {
            vote.setUser(user);
            em.merge(vote);
        }
    }

}
