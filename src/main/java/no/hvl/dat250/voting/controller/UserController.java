package no.hvl.dat250.voting.controller;

import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserDao userDao;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userDao.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        return userDao.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        User user = userDao.findUserById(id);
        if(user != null) {
            userDao.deleteUser(user);
        }
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User newUser) {
        // Additional logic to handle updating a user might go here.
        return userDao.updateUser(newUser);
    }

    @GetMapping("/{id}/polls")
    public List<Poll> getPollsByUser(@PathVariable Long id) {
        return userDao.getPollsByUser(id);
    }


}
