package no.hvl.dat250.voting.controller;

import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.Vote;
import no.hvl.dat250.voting.dao.VoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    @Autowired
    private VoteDao voteDao;

    @PostMapping
    public Vote createVote(@RequestBody Vote vote) {
        return voteDao.createGroup(vote);
    }

    @GetMapping
    public List<Vote> getAllVotes() {
        return voteDao.getAllVotes();
    }

    @GetMapping("/{id}")
    public Vote findVoteById(@PathVariable Long id) {
        return voteDao.findGroupById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVote(@PathVariable Long id) {
        Vote vote = voteDao.findGroupById(id);
        if(vote != null) {
            voteDao.deleteGroup(vote);
        }
    }

    @PutMapping("/{id}")
    public Vote updateVote(@PathVariable Long id, @RequestBody Vote newVote) {
        // Additional logic for updating might be placed here
        return voteDao.updateGroup(newVote);
    }

    @GetMapping("/user/{userId}")
    public List<Vote> getVotesByUser(@PathVariable Long userId) {
        return voteDao.getVotesByUser(userId);
    }

    // Assuming you have Poll object with an id field
    @GetMapping("/poll/{pollId}")
    public List<Vote> getVotesByPoll(@PathVariable Long pollId) {
        Poll poll = new Poll();
        return voteDao.getVotesByPoll(poll);
    }
}
