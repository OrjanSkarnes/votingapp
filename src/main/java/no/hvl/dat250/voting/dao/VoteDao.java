package no.hvl.dat250.voting.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.Vote;

import java.util.List;

@AllArgsConstructor
public class VoteDao {

    @PersistenceContext(unitName = "votingapp")
    private EntityManager em;

    public Vote createGroup(Vote vote) {
        em.getTransaction().begin();
        em.persist(vote);
        em.getTransaction().commit();
        return vote;
    }

    public Vote findGroupById(Long id) {
        return em.find(Vote.class, id);
    }

    public void deleteGroup(Vote vote) {
        em.getTransaction().begin();
        em.remove(vote);
        em.getTransaction().commit();
    }

    public Vote updateGroup(Vote vote) {
        em.getTransaction().begin();
        em.merge(vote);
        em.getTransaction().commit();
        return vote;
    }

    public List<Vote> getAllVotes() {
        TypedQuery<Vote> query = em.createQuery("SELECT v FROM Vote v", Vote.class);
        return query.getResultList();
    }

    public List<Vote> getVotesByUser(Long userId) {
        TypedQuery<Vote> query = em.createQuery("SELECT v FROM Vote v WHERE v.user.id = :userId", Vote.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Vote> getVotesByPoll(Poll poll) {
        TypedQuery<Vote> query = em.createQuery("SELECT v FROM Vote v WHERE v.poll = :poll", Vote.class);
        query.setParameter("poll", poll);
        return query.getResultList().stream().toList();
    }
}
