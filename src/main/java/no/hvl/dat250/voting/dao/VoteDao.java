package no.hvl.dat250.voting.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.Vote;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class VoteDao {

    @PersistenceContext
    private EntityManager em;

    public Vote createVote(Vote vote) {
        em.persist(vote);
        return vote;
    }

    public Vote findVoteById(Long id) {
        return em.find(Vote.class, id);
    }

    public void deleteVote(Vote vote) {
        em.remove(vote);
    }

    public Vote updateVote(Vote vote) {
        em.merge(vote);
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

    public List<Vote> getVotesByUserAndPoll(User user, Poll poll) {
        return em.createQuery("SELECT v FROM Vote v WHERE v.user = :user AND v.poll = :poll", Vote.class)
            .setParameter("user", user)
            .setParameter("poll", poll)
            .getResultList();
    }

    public List<Vote> getVotesByTempIdAndPoll(Long tempId, Poll poll) {
        return em.createQuery("SELECT v FROM Vote v WHERE v.tempId = :tempId AND v.poll = :poll", Vote.class)
            .setParameter("tempId", tempId)
            .setParameter("poll", poll)
            .getResultList();
    }
}
