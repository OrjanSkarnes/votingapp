package no.hvl.dat250.voting.controller;

import no.hvl.dat250.voting.Group;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public Group findGroupById(@PathVariable Long id) {
        return groupService.findGroupById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
    }

    @PutMapping("/{id}")
    public Group updateGroup(@PathVariable Long id, @RequestBody Group newGroup) {
        // Add logic to handle ID mismatch or other requirements as needed
        return groupService.updateGroup(id, newGroup);
    }

    @GetMapping("/user")
    public List<Group> getGroupsByUser(@RequestBody User user) {
        return groupService.getGroupsByUser(user);
    }

    @GetMapping("/poll")
    public List<Group> getGroupsByPoll(@RequestBody Poll poll) {
        return groupService.getGroupsByPoll(poll);
    }
}
