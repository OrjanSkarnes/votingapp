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
        // Get the voes from a user and based on that vote get all the polls
        TypedQuery<Poll> query = em.createQuery("SELECT p FROM Poll p WHERE p.votes = :votes", Poll.class);
        return query.getResultList().stream().filter(poll -> poll.getVotes().stream().anyMatch(vote -> vote.getUser().getId().equals(userId))).toList();
    }

    public List<Integer> getVotesForAndAgainstPoll(Long pollId) {
        TypedQuery<Integer> query = em.createQuery("SELECT COUNT(v) FROM Vote v WHERE v.poll.id = :pollId AND v.choice = true", Integer.class);
        TypedQuery<Integer> query2 = em.createQuery("SELECT COUNT(v) FROM Vote v WHERE v.poll.id = :pollId AND v.choice = false", Integer.class);
        query.setParameter("pollId", pollId);
        query2.setParameter("pollId", pollId);
        return List.of(query.getSingleResult(), query2.getSingleResult());
    }
}
