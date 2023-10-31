package no.hvl.dat250.voting.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import no.hvl.dat250.voting.Poll;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class PollDao {

    @PersistenceContext
    private EntityManager em;

    public Poll createPoll(Poll poll) {
        em.persist(poll);
        return poll;
    }

    public Poll findPollById(Long id) {
        return em.find(Poll.class, id);
    }

    public void deletePoll(Poll poll) {
        em.remove(poll);
    }

    public Poll updatePoll(Poll poll) {
        em.merge(poll);
        return poll;
    }

    public List<Poll> getAllPolls() {
        TypedQuery<Poll> query = em.createQuery("SELECT p FROM Poll p", Poll.class);
        return query.getResultList();
    }

    public List<Poll> getPollsByUser(Long userId) {
        TypedQuery<Poll> query = em.createQuery("SELECT p FROM Poll p JOIN p.votes v WHERE v.user.id = :userId", Poll.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Poll> getPollsCreatedByUser(Long userId) {
        TypedQuery<Poll> query = em.createQuery("SELECT p FROM Poll p WHERE p.creator.id = :userId", Poll.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Poll> getPollsBasedOnVotesFromUser(Long userId, Long tempId) {
        TypedQuery<Poll> query;
        if (userId != null) {
            query = em.createQuery("SELECT p FROM Poll p JOIN p.votes v WHERE v.user.id = :userId", Poll.class);
            query.setParameter("userId", userId);
        }
        else if (tempId != null) {
            query = em.createQuery("SELECT p FROM Poll p JOIN p.votes v WHERE v.tempId = :tempId", Poll.class);
            query.setParameter("tempId", tempId);
        } else {
            // Handle case where both userId and tempId are null
            throw new IllegalArgumentException("Both userId and tempId cannot be null");
        }
        return query.getResultList();
    }
}
