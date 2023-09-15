package no.hvl.dat250.voting.driver;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.Roles;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.Vote;
import no.hvl.dat250.voting.dao.PollDao;
import no.hvl.dat250.voting.dao.UserDao;
import no.hvl.dat250.voting.dao.VoteDao;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VotingMainTest {
    private EntityManagerFactory factory;

    @Before
    public void setUp() {
        factory = Persistence.createEntityManagerFactory(VotingMain.PERSISTENCE_UNIT_NAME);
    }

    private User creatUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("1234");
        user.setRole(Roles.USER);

        return user;
    }


    private Poll createPoll(String question) {
        Poll poll = new Poll();
        poll.setQuestion(question);
        poll.setPrivateAccess(true);
        poll.setActive(true);
        return poll;
    }



    @Test
    public void testDomainModelPersistenceAndDao() {
        VotingMain.main(new String[]{});

        EntityManager em = factory.createEntityManager();

        UserDao userDao = new UserDao(em);
        PollDao pollDao = new PollDao(em);
        VoteDao voteDao = new VoteDao(em);

        User user = userDao.findUserById(1L);
        User voter = userDao.findUserById(2L);

        List<Poll> polls = pollDao.getPollsByUser(2L);
        List<Vote> votes = voteDao.getVotesByPoll(polls.get(0));

        assertThat(user.getUsername(), is("test"));
        assertThat(user.getPassword(), is("test"));

        assertThat(polls, is(voter.getParticipatedPolls()));
        assertThat(votes, is(voter.getVotes()));

    }

    @Test
    public void testCreatingUserWithDao() {
        VotingMain.main(new String[]{});

        EntityManager em = factory.createEntityManager();

        UserDao userDao = new UserDao(em);
    
        User newUser = creatUser("Gandalf");

        userDao.createUser(newUser);

        List<User> users = userDao.getAllUsers();
        User user = users.get(users.size() - 1);


        assertThat(users.size(), is(3));
        assertThat(user.getUsername(), is("Gandalf"));
        assertThat(user, is(newUser));
    }

    @Test
    public void testPollDao() {
            VotingMain.main(new String[]{});

        EntityManager em = factory.createEntityManager();
        PollDao pollDao = new PollDao(em);

        Poll poll = createPoll("Test question");
        // Save poll to database
        pollDao.createPoll(poll);

        Poll retrievedPoll = pollDao.findPollById(poll.getId());

        assertThat(retrievedPoll, is(poll));

        retrievedPoll.setQuestion("Updated question");
        pollDao.updatePoll(retrievedPoll);

        Poll updatedPoll = pollDao.findPollById(poll.getId());

        assertThat(updatedPoll.getQuestion(), is("Updated question"));

        pollDao.deletePoll(updatedPoll);

        assertNull(pollDao.findPollById(updatedPoll.getId()));
    }

}
