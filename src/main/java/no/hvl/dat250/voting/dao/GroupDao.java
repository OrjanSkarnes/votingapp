package no.hvl.dat250.voting.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import no.hvl.dat250.voting.models.Group;
import no.hvl.dat250.voting.models.Poll;
import no.hvl.dat250.voting.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class GroupDao {

    @PersistenceContext
    private EntityManager em;

    public Group createGroup(Group group) {
        em.persist(group);
        return group;
    }

    public Group findGroupById(Long id) {
        return em.find(Group.class, id);
    }

    public void deleteGroup(Group group) {
        em.remove(group);
    }

    public Group updateGroup(Group group) {
        em.merge(group);
        return group;
    }

    public List<Group> getAllGroups() {
        TypedQuery<Group> query = em.createQuery("SELECT g FROM Group g", Group.class);
        return query.getResultList();
    }

    public List<Group> getGroupsByUser(User user) {
        TypedQuery<Group> query = em.createQuery("SELECT g FROM Group g WHERE :user MEMBER OF g.members", Group.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    public List<Group> getGroupsByPoll(Poll poll) {
        TypedQuery<Group> query = em.createQuery("SELECT g FROM Group g WHERE :poll MEMBER OF g.polls", Group.class);
        query.setParameter("poll", poll);
        return query.getResultList();
    }
}
