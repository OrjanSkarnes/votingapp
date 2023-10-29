package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.Vote;
import no.hvl.dat250.voting.dao.PollDao;
import no.hvl.dat250.voting.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class PollService {

    @Autowired
    private PollDao pollDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    public Poll createPoll(Poll poll) {
        return pollDao.createPoll(poll);
    }

    @Transactional(readOnly = true)
    public List<Poll> getAllPolls() {
        return pollDao.getAllPolls();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Poll> findPollById(Long id) {
        HttpStatus status = HttpStatus.OK;

        Poll poll = pollDao.findPollById(id);
        if(poll != null) {
            return new ResponseEntity<>(poll, status);
        }

        status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(null, status);
    }

    @Transactional
    public void deletePoll(Long id) {
        Poll poll = pollDao.findPollById(id);
        if(poll != null) {
            pollDao.deletePoll(poll);
        }
    }

    @Transactional
    public Poll updatePoll(Long id, Poll newPoll) {
        if (!newPoll.getId().equals(id)) {
            return null;
        }
        return pollDao.updatePoll(newPoll);
    }

    @Transactional(readOnly = true)
    public List<Poll> getPollsByUser(@PathVariable Long userId) {
        return pollDao.getPollsByUser(userId);
    }

    @Transactional
    public List<Poll> getPollsBasedOnVotesFromUser(@PathVariable Long userId) {
        return pollDao.getPollsBasedOnVotesFromUser(userId);
    }
}
