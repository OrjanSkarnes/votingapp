package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.Vote;
import no.hvl.dat250.voting.DTO.PollDTO;
import no.hvl.dat250.voting.DTO.UserDTO;
import no.hvl.dat250.voting.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    public ResponseEntity<UserDTO> createUser(User user, Long tempId) {
        // Attempt to fetch the user
        User userFromDb = userDao.findUserByUserName(user.getUsername());

        // If the user already exists, return the userName with message user already exists
        if(userFromDb != null && userFromDb.getUsername().equals(user.getUsername())) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        userFromDb = userDao.createUser(user, tempId);

        return new ResponseEntity<>(UserDTO.convertToDTO(userFromDb), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<UserDTO> loginUser(User user) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        // Attempt to fetch the user
        User userFromDb = userDao.findUserByUserName(user.getUsername());
        // Check if user exists and the password is correct
        if (userFromDb != null && userFromDb.getPassword().equals(user.getPassword())) {
            status = HttpStatus.OK;
            return new ResponseEntity<>(UserDTO.convertToDTO(userFromDb), status);
        }

        // If only the username is incorrect
        if (userFromDb == null) {
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(null, status);
    }

    @Transactional
    public UserDTO findUserById(Long id) {
        return UserDTO.convertToDTO(userDao.findUserById(id));
    }

    @Transactional
    public UserDTO findUserByUserName(String username) {
        return UserDTO.convertToDTO(userDao.findUserByUserName(username));
    }
    @Transactional
    public void deleteUser(Long id) {
        User user = userDao.findUserById(id);
        if(user != null) {
            userDao.deleteUser(user);
        }
    }
    @Transactional
    public UserDTO updateUser(User user) {
        return UserDTO.convertToDTO(userDao.updateUser(user));
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userDao.getAllUsers().stream().map(UserDTO::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Poll> getPollsByUser(Long userId) {
        return userDao.getPollsByUser(userId);
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getPollsBasedOnVotesFromUser(Long userId) {
        List<Vote> votes = userDao.getVotesByUser(userId);
        return votes.stream()
                .map(Vote::getPoll)
                .distinct()
                .map(PollDTO::convertToDTO)
                .collect(Collectors.toList());
    }
}
