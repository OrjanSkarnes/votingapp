package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.Vote;
import no.hvl.dat250.voting.dao.PollDao;
import no.hvl.dat250.voting.dao.VoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class VoteService {

    @Autowired
    private VoteDao voteDao;

    @Autowired
    private PollDao pollDao;

    @Transactional
    public Vote createVote(@RequestBody Vote vote) {
        return voteDao.createVote(vote);
    }

    @Transactional(readOnly = true)
    public List<Vote> getAllVotes() {
        return voteDao.getAllVotes();
    }

    @Transactional(readOnly = true)
    public Vote findVoteById(@PathVariable Long id) {
        return voteDao.findVoteById(id);
    }

    @Transactional
    public void deleteVote(@PathVariable Long id) {
        Vote vote = voteDao.findVoteById(id);
        if(vote != null) {
            voteDao.deleteVote(vote);
        }
    }

    @Transactional
    public Vote updateVote(@PathVariable Long id, @RequestBody Vote newVote) {

        if (!newVote.getVoteId().equals(id)) {
            return null;
        }
        // Additional logic for updating might be placed here
        return voteDao.updateVote(newVote);
    }

    @Transactional
    public List<Vote> getVotesByUser(@PathVariable Long userId) {
        return voteDao.getVotesByUser(userId);
    }

    @Transactional
    public List<Vote> getVotesByPoll(@PathVariable Long pollId) {
        Poll poll = pollDao.findPollById(pollId);
        return voteDao.getVotesByPoll(poll);
    }
}
