package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.models.Poll;
import no.hvl.dat250.voting.models.User;
import no.hvl.dat250.voting.models.Vote;
import no.hvl.dat250.voting.DTO.PollDTO;
import no.hvl.dat250.voting.DTO.UserDTO;
import no.hvl.dat250.voting.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<UserDTO> createUser(User user, Long tempId) {
        // Attempt to fetch the user
        User existingUser = userDao.findUserByUserName(user.getUsername());

        // If the user already exists, return a conflict status
        if (existingUser != null) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Create the user
        User createdUser = userDao.createUser(user, tempId);

        // Convert to DTO and return
        return new ResponseEntity<>(UserDTO.convertToDTO(createdUser), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<UserDTO> loginUser(User loginUser) {
        User userFromDb = userDao.findUserByUserName(loginUser.getUsername());

        if (userFromDb != null && passwordEncoder.matches(loginUser.getPassword(), userFromDb.getPassword())) {
            // Authentication successful
            return new ResponseEntity<>(UserDTO.convertToDTO(userFromDb), HttpStatus.OK);
        } else {
            // Authentication failed
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
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


    public UserDTO findUserByUsername(String username) {
        return UserDTO.convertToDTO(userDao.findUserByUserName(username));
    }
}
