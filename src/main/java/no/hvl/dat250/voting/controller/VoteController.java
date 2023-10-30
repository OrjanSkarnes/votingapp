package no.hvl.dat250.voting.controller;

import no.hvl.dat250.voting.DTO.VoteDTO;
import no.hvl.dat250.voting.Vote;
import no.hvl.dat250.voting.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping
    public VoteDTO createVote(@RequestBody Vote vote) {
        return voteService.createVote(vote);
    }

    @GetMapping
    public List<VoteDTO> getAllVotes() {
        return voteService.getAllVotes();
    }

    @GetMapping("/{id}")
    public VoteDTO findVoteById(@PathVariable Long id) {
        return voteService.findVoteById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVote(@PathVariable Long id) {
        voteService.deleteVote(id);
    }

    @PutMapping("/{id}")
    public VoteDTO updateVote(@PathVariable Long id, @RequestBody Vote newVote) {
        return voteService.updateVote(id, newVote);
    }

    @GetMapping("/user/{userId}")
    public List<VoteDTO> getVotesByUser(@PathVariable Long userId) {
        return voteService.getVotesByUser(userId);
    }

    // Assuming you have Poll object with an id field
    @GetMapping("/poll/{pollId}")
    public List<VoteDTO> getVotesByPoll(@PathVariable Long pollId) {
        return voteService.getVotesByPoll(pollId);
    }

    @GetMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }
}
