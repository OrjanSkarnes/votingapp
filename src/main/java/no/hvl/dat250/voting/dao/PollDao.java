package no.hvl.dat250.voting.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import no.hvl.dat250.voting.Poll;

import java.util.List;

@AllArgsConstructor
public class PollDao {

    @PersistenceContext(unitName = "votingapp")
    private EntityManager em;

    public Poll createPoll(Poll poll) {
        em.getTransaction().begin();
        em.persist(poll);
        em.getTransaction().commit();
        return poll;
    }

    public Poll findPollById(Long id) {
        return em.find(Poll.class, id);
    }

    public void deletePoll(Poll poll) {
        em.getTransaction().begin();
        em.remove(poll);
        em.getTransaction().commit();
    }

    public Poll updatePoll(Poll poll) {
        em.getTransaction().begin();
        em.merge(poll);
        em.getTransaction().commit();
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
}
