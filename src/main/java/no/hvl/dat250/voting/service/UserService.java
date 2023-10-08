package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*public User createUser(User user) {
        em.persist(user);
        return user;
        }

public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class)
        .getResultList();
        }

public User findUserById(Long id) {
        return em.find(User.class, id);
        }

public void deleteUser(User user) {
        em.remove(user);
        }

public User updateUser(User user) {
        em.merge(user);
        return user;
        }

public List<Poll> getPollsByUser(Long userId) {
        return em.createQuery("SELECT p FROM Poll p WHERE p.creator.id = :userId", Poll.class)
        .setParameter("userId", userId)
        .getResultList();
        }*/
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    public User createUser(User user) {
        if(userDao.findUserByUserName(user.getUsername()) != null) {
            return null;
        }
        return userDao.createUser(user);
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
