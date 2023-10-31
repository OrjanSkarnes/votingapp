package no.hvl.dat250.voting.controller;

import no.hvl.dat250.voting.DTO.PollDTO;
import no.hvl.dat250.voting.DTO.VoteDTO;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class PollController {

    @Autowired
    private PollService pollService;

    @PostMapping
    public PollDTO createPoll(@RequestBody Poll poll) {
        return pollService.createPoll(poll);
    }

    @GetMapping
    public List<PollDTO> getAllPolls() {
        return pollService.getAllPolls();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollDTO> findPollById(@PathVariable Long id) {
        return pollService.findPollById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePoll(@PathVariable Long id) {
        pollService.deletePoll(id);
    }

    @PutMapping("/{id}")
    public PollDTO updatePoll(@PathVariable Long id, @RequestBody Poll newPoll) {
        return pollService.updatePoll(id, newPoll);
    }

    @GetMapping("/user/{userId}")
    public List<PollDTO> getPollsByUser(@PathVariable Long userId) {
        return pollService.getPollsByUser(userId);
    }

    @GetMapping("/user/{userId}/votes")
    public List<PollDTO> getPollsBasedOnVotesFromUser(@PathVariable Long userId) {
        return pollService.getPollsBasedOnVotesFromUser(userId);
    }
    //@GetMapping(value = "/{path:[^\\.]*}")
    //public String redirect() {
        //return "forward:/index.html";    }
}
