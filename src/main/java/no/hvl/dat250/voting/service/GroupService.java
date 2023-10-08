package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.Group;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.dao.GroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupDao groupDao;

    @Transactional
    public Group createGroup(Group group) {
        return groupDao.createGroup(group);
    }

    @Transactional(readOnly = true)
    public List<Group> getAllGroups() {
        return groupDao.getAllGroups();
    }

    @Transactional(readOnly = true)
    public Group findGroupById(Long id) {
        return groupDao.findGroupById(id);
    }

    @Transactional
    public void deleteGroup(Long id) {
        Group group = groupDao.findGroupById(id);
        if(group != null) {
            groupDao.deleteGroup(group);
        }
    }
    @Transactional
    public Group updateGroup(Long id, Group newGroup) { return groupDao.updateGroup(newGroup); }

    @Transactional(readOnly = true)
    public List<Group> getGroupsByUser(User user) {
        return groupDao.getGroupsByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Group> getGroupsByPoll(Poll poll) {
        return groupDao.getGroupsByPoll(poll);
    }
}
