package no.hvl.dat250.voting.controller;

import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.dao.PollDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    private PollDao pollDao;

    @PostMapping
    public Poll createPoll(@RequestBody Poll poll) {
        return pollDao.createPoll(poll);
    }

    @GetMapping
    public List<Poll> getAllPolls() {
        return pollDao.getAllPolls();
    }

    @GetMapping("/{id}")
    public Poll findPollById(@PathVariable Long id) {
        return pollDao.findPollById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePoll(@PathVariable Long id) {
        Poll poll = pollDao.findPollById(id);
        if(poll != null) {
            pollDao.deletePoll(poll);
        }
    }

    @PutMapping("/{id}")
    public Poll updatePoll(@PathVariable Long id, @RequestBody Poll newPoll) {
        // Ensure the ID from path variable and poll object are the same
        // Add additional logic as necessary, for example, to prevent ID mismatch
        return pollDao.updatePoll(newPoll);
    }

    @GetMapping("/user/{userId}")
    public List<Poll> getPollsByUser(@PathVariable Long userId) {
        return pollDao.getPollsByUser(userId);
    }
}
