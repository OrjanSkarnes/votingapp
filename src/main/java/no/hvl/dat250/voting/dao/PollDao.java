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

    public List<Poll> getPollsBasedOnVotesFromUser(Long userId) {
        TypedQuery<Poll> query = em.createQuery("SELECT p FROM Poll p JOIN p.votes v WHERE v.user.id = :userId", Poll.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
