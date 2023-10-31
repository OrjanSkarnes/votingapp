package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.DTO.VoteDTO;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.Vote;
import no.hvl.dat250.voting.dao.PollDao;
import no.hvl.dat250.voting.dao.UserDao;
import no.hvl.dat250.voting.dao.VoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoteService {

    @Autowired
    private VoteDao voteDao;

    @Autowired
    private PollDao pollDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    public ResponseEntity<VoteDTO> createVote(VoteDTO voteDTO, Long tempId) {
        User user = userDao.findUserById(voteDTO.getUserId());
        Poll poll = pollDao.findPollById(voteDTO.getPollId());
        if (poll != null) {
            if (poll.isPrivateAccess() && user == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            if (user != null)  {
                List<Vote> existingVote = voteDao.getVotesByUserAndPoll(user, poll);
                if (!existingVote.isEmpty()) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
            }
            if (tempId != null) {
                List<Vote> existingVote = voteDao.getVotesByTempIdAndPoll(tempId, poll);
                if (!existingVote.isEmpty()) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
            }
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setTempId(tempId != null ? tempId : null);
            vote.setPoll(poll);
            vote.setTimestamp(LocalDateTime.now());
            vote.setChoice(vote.getChoice());
            return new ResponseEntity<>(VoteDTO.convertToDTO(voteDao.createVote(vote)), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public List<VoteDTO> getAllVotes() {
        return VoteDTO.convertToListOfDTO(voteDao.getAllVotes());
    }

    @Transactional(readOnly = true)
    public VoteDTO findVoteById(@PathVariable Long id) {
        return VoteDTO.convertToDTO(voteDao.findVoteById(id));
    }

    @Transactional
    public void deleteVote(@PathVariable Long id) {
        Vote vote = voteDao.findVoteById(id);
        if(vote != null) {
            voteDao.deleteVote(vote);
        }
    }

    @Transactional
    public VoteDTO updateVote(@PathVariable Long id, @RequestBody Vote newVote) {

        if (!newVote.getVoteId().equals(id)) {
            return null;
        }
        // Additional logic for updating might be placed here
        return  VoteDTO.convertToDTO(voteDao.updateVote(newVote));
    }

    @Transactional
    public List<VoteDTO> getVotesByUser(@PathVariable Long userId) {
        return VoteDTO.convertToListOfDTO(voteDao.getVotesByUser(userId));
    }

    @Transactional
    public List<VoteDTO> getVotesByPoll(@PathVariable Long pollId) {
        Poll poll = pollDao.findPollById(pollId);
        return VoteDTO.convertToListOfDTO(voteDao.getVotesByPoll(poll));
    }
}
