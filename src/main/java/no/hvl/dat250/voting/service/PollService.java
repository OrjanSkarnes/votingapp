package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.dao.PollDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class PollService {

    @Autowired
    private PollDao pollDao;

    @Transactional
    public Poll createPoll(Poll poll) {
        return pollDao.createPoll(poll);
    }

    @Transactional(readOnly = true)
    public List<Poll> getAllPolls() {
        return pollDao.getAllPolls();
    }

    @Transactional(readOnly = true)
    public Poll findPollById(Long id) {
        return pollDao.findPollById(id);
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
}
