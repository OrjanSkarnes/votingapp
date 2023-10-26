package no.hvl.dat250.voting.controller;

import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    private PollService pollService;

    @PostMapping
    public Poll createPoll(@RequestBody Poll poll) {
        return pollService.createPoll(poll);
    }

    @GetMapping
    public List<Poll> getAllPolls() {
        return pollService.getAllPolls();
    }

    @GetMapping("/{id}")
    public Poll findPollById(@PathVariable Long id) {
        return pollService.findPollById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePoll(@PathVariable Long id) {
        pollService.deletePoll(id);
    }

    @PutMapping("/{id}")
    public Poll updatePoll(@PathVariable Long id, @RequestBody Poll newPoll) {
        return pollService.updatePoll(id, newPoll);
    }

    @GetMapping("/user/{userId}")
    public List<Poll> getPollsByUser(@PathVariable Long userId) {
        return pollService.getPollsByUser(userId);
    }

    @GetMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";    }
}
