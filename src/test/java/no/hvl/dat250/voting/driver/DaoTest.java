package no.hvl.dat250.voting.driver;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.voting.Poll;
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

public class DaoTest {

    private EntityManagerFactory factory;

    @Before
    public void setUp() {
        factory = Persistence.createEntityManagerFactory(VotingMain.PERSISTENCE_UNIT_NAME);
    }

    @Test
    public void testAllDao() {
        // Run the main class to persist the objects.
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
}
