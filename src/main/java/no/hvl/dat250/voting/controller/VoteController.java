package no.hvl.dat250.voting.controller;

import no.hvl.dat250.voting.Vote;
import no.hvl.dat250.voting.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping
    public Vote createVote(@RequestBody Vote vote) {
        return voteService.createVote(vote);
    }

    @GetMapping
    public List<Vote> getAllVotes() {
        return voteService.getAllVotes();
    }

    @GetMapping("/{id}")
    public Vote findVoteById(@PathVariable Long id) {
        return voteService.findVoteById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVote(@PathVariable Long id) {
        voteService.deleteVote(id);
    }

    @PutMapping("/{id}")
    public Vote updateVote(@PathVariable Long id, @RequestBody Vote newVote) {
        return voteService.updateVote(id, newVote);
    }

    @GetMapping("/user/{userId}")
    public List<Vote> getVotesByUser(@PathVariable Long userId) {
        return voteService.getVotesByUser(userId);
    }

    // Assuming you have Poll object with an id field
    @GetMapping("/poll/{pollId}")
    public List<Vote> getVotesByPoll(@PathVariable Long pollId) {
        return voteService.getVotesByPoll(pollId);
    }
}
