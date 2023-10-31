package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.DTO.PollDTO;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.dao.PollDao;
import no.hvl.dat250.voting.dao.VoteDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {

    @Autowired
    private PollDao pollDao;

    @Transactional
    public PollDTO createPoll(Poll poll) {
        Poll createPoll = pollDao.createPoll(poll);
        return PollDTO.convertToDTO(createPoll);
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getAllPolls() {
        List<Poll> polls = pollDao.getAllPolls();
        return polls.stream().map(PollDTO::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<PollDTO> findPollById(Long id) {
        HttpStatus status = HttpStatus.OK;

        Poll poll = pollDao.findPollById(id);
        if(poll != null) {
            PollDTO pollDTO = PollDTO.convertToDTO(poll);
            return new ResponseEntity<>(pollDTO, status);
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
    public PollDTO updatePoll(Long id, Poll newPoll) {
        if (!newPoll.getId().equals(id)) {
            return null;
        }
        return PollDTO.convertToDTO(pollDao.updatePoll(newPoll));
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getPollsByUser(@PathVariable Long userId) {
        return pollDao.getPollsByUser(userId).stream().map(PollDTO::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<PollDTO> getPollsBasedOnVotesFromUser(@PathVariable Long userId) {
        return pollDao.getPollsBasedOnVotesFromUser(userId).stream().map(PollDTO::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public void finishPoll(@PathVariable Long id) {
        Poll poll = pollDao.findPollById(id);
        if(poll != null) {
            poll.setActive(false);
            pollDao.updatePoll(poll);
        }
    }
}
