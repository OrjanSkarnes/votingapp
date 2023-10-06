package no.hvl.dat250.voting.controller;

import no.hvl.dat250.voting.Group;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.dao.GroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupDao groupDao;

    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupDao.createGroup(group);
    }

    @GetMapping
    public List<Group> getAllGroups() {
        return groupDao.getAllGroups();
    }

    @GetMapping("/{id}")
    public Group findGroupById(@PathVariable Long id) {
        return groupDao.findGroupById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        Group group = groupDao.findGroupById(id);
        if(group != null) {
            groupDao.deleteGroup(group);
        }
    }

    @PutMapping("/{id}")
    public Group updateGroup(@PathVariable Long id, @RequestBody Group newGroup) {
        // Add logic to handle ID mismatch or other requirements as needed
        return groupDao.updateGroup(newGroup);
    }

    @GetMapping("/user")
    public List<Group> getGroupsByUser(@RequestBody User user) {
        return groupDao.getGroupsByUser(user);
    }

    @GetMapping("/poll")
    public List<Group> getGroupsByPoll(@RequestBody Poll poll) {
        return groupDao.getGroupsByPoll(poll);
    }
}
