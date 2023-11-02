package no.hvl.dat250.voting.controller;

import no.hvl.dat250.voting.DTO.PollDTO;
import no.hvl.dat250.voting.DTO.VoteDTO;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PollDTO createPoll(@RequestBody PollDTO poll) {
        return pollService.createPoll(poll);
    }

    @GetMapping
    public List<PollDTO> getAllPolls() {
        return pollService.getAllPolls();
    }

    @GetMapping("/public")
    public List<PollDTO> getAllPublicPolls() {
        return pollService.getAllPublicPolls();
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
    public PollDTO updatePoll(@PathVariable Long id, @RequestBody PollDTO newPoll) {
        return pollService.updatePoll(id, newPoll);
    }

    @GetMapping("/user/{userId}")
    public List<PollDTO> getPollsByUser(@PathVariable Long userId) {
        return pollService.getPollsByUser(userId);
    }

    @GetMapping("/user/{userId}/votes")
    public List<PollDTO> getPollsBasedOnVotesFromUser(@PathVariable(required = false) Long userId, @RequestParam(required = false) Long tempId) {
        return pollService.getPollsBasedOnVotesFromUser(userId, tempId);
    }

    @GetMapping("/user/{userId}/created")
    public List<PollDTO> getPollsCreatedByUser(@PathVariable Long userId) {
        return pollService.getPollsCreatedByUser(userId);
    }

    @GetMapping("/{id}/votes")
    public List<VoteDTO> getVotesByPoll(@PathVariable Long id) {
        return pollService.getVotesByPoll(id);
    }


    //@GetMapping(value = "/{path:[^\\.]*}")
    //public String redirect() {
        //return "forward:/index.html";    }
}
