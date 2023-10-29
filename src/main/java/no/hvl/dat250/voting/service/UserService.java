package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    public ResponseEntity<?> createUser(User user) {
        // Attempt to fetch the user
        User userFromDb = userDao.findUserByUserName(user.getUsername());

        // If the user already exists, return the userName with message user already exists
        if(userFromDb != null && userFromDb.getUsername().equals(user.getUsername())) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        // If the user does not exist, create the user
        userFromDb = userDao.createUser(user);

        return new ResponseEntity<>(userFromDb, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<User> loginUser(User user) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        // Attempt to fetch the user
        User userFromDb = userDao.findUserByUserName(user.getUsername());

        // Check if user exists and the password is correct
        if (userFromDb != null && userFromDb.getPassword().equals(user.getPassword())) {
            status = HttpStatus.OK;
            return new ResponseEntity<>(userFromDb, status);
        }

        // If only the username is incorrect
        if (userFromDb == null) {
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(null, status);
    }

    @Transactional
    public User findUserById(Long id) {
        return userDao.findUserById(id);
    }

    @Transactional
    public User findUserByUserName(String username) {
        return userDao.findUserByUserName(username);
    }
    @Transactional
    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }
    @Transactional
    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Transactional(readOnly = true)
    public List<Poll> getPollsByUser(Long userId) {
        return userDao.getPollsByUser(userId);
    }
}
