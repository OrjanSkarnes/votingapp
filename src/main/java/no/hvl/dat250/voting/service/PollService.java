package no.hvl.dat250.voting.service;
import no.hvl.dat250.voting.DTO.PollDTO;
import no.hvl.dat250.voting.DTO.VoteDTO;
import no.hvl.dat250.voting.models.Poll;
import no.hvl.dat250.voting.models.User;
import no.hvl.dat250.voting.dao.PollDao;
import no.hvl.dat250.voting.dao.UserDao;

import no.hvl.dat250.voting.models.EndTimeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PollService {

    @Value("${spring.kafka.poll-stream-topic}")
    private String pollStreamTopic;

    @Autowired
    private PollDao pollDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private KafkaProducerService kafkaProducer;

    @Transactional
    public PollDTO createPoll(PollDTO poll) {
        User creator = userDao.findUserById(poll.getCreatorId());
        Poll createPoll = new Poll();
        createPoll.setQuestion(poll.getQuestion());
        createPoll.setDescription(poll.getDescription());
        createPoll.setStartTime(poll.getStartTime());
        createPoll.setEndTime(poll.getEndTime());
        createPoll.setActive(poll.isActive());
        createPoll.setPrivateAccess(poll.isPrivateAccess());
        createPoll.setCreator(creator);
        Poll poll1 = pollDao.createPoll(createPoll);

        if (poll1.getEndTime() != null)  {
            schedulePollEndTime(createPoll);
        }
        return PollDTO.convertToDTO(poll1);
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getAllPolls() {
        List<Poll> polls = pollDao.getAllPolls();
        return polls.stream().map(PollDTO::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getAllPublicPolls() {
        List<Poll> polls = pollDao.getAllPublicPolls();
        return polls.stream().map(PollDTO::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<PollDTO> findPollById(Long id) {
        HttpStatus status = HttpStatus.OK;

        Poll poll = pollDao.findPollById(id);
        if(poll != null) {
            PollDTO pollDTO = PollDTO.convertToDTO(poll);
            return new ResponseEntity<>(pollDTO, status);
        }

        status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(null, status);
    }

    @Transactional
    public void deletePoll(Long id) {
        Poll poll = pollDao.findPollById(id);
        if(poll != null) {
            pollDao.deletePoll(poll);
        }
    }

    @Transactional
    public PollDTO updatePoll(Long id, PollDTO newPoll) {
        if (!newPoll.getId().equals(id)) {
            return null;
        }
        Poll poll = pollDao.findPollById(id);
        // Update poll with new values
        poll.setQuestion(newPoll.getQuestion());
        poll.setDescription(newPoll.getDescription());
        poll.setStartTime(newPoll.getStartTime());
        poll.setEndTime(newPoll.getEndTime());
        poll.setActive(newPoll.isActive());
        poll.setPrivateAccess(newPoll.isPrivateAccess());

        if (poll.getEndTime() != null)  {
            schedulePollEndTime(poll);
        }
        return PollDTO.convertToDTO(pollDao.updatePoll(poll));
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getPollsByUser(@PathVariable Long userId) {
        return pollDao.getPollsByUser(userId).stream().map(PollDTO::converteToDTOwithVotes).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getPollsCreatedByUser(@PathVariable Long userId) {
        return pollDao.getPollsCreatedByUser(userId).stream().map(PollDTO::converteToDTOwithVotes).collect(Collectors.toList());
    }

    @Transactional
    public List<PollDTO> getPollsBasedOnVotesFromUser(@PathVariable Long userId, Long tempId) {
        return pollDao.getPollsBasedOnVotesFromUser(userId, tempId).stream().map(PollDTO::converteToDTOwithVotes).collect(Collectors.toList());
    }

    @Transactional
    public List<VoteDTO> getVotesByPoll(@PathVariable Long id) {
        return pollDao.findPollById(id).getVotes().stream().map(VoteDTO::convertToDTO).collect(Collectors.toList());
    }

    public void finishPoll(Long id) {
        Poll poll = pollDao.findPollById(id);
        if(poll != null) {
            poll.setActive(false);
            pollDao.updatePoll(poll);
            sendPollResultsToKafka(poll);
        }
    }

    public void sendPollResultsToKafka(Poll poll) {
        // Send the pollresults to kafka
        if (!poll.isActive()) {
            kafkaProducer.sendObject("pollResults", poll);
        }
    }

    public void schedulePollEndTime(Poll poll) {
        if (poll.getId() != null && poll.getEndTime() != null) {
            EndTimeInfo endTimeInfo = new EndTimeInfo(poll.getId(), poll.getEndTime());
            kafkaProducer.sendObject(pollStreamTopic, endTimeInfo);
        }

    }

    @Transactional
    @KafkaListener(topics = "pollEndTimesFinished")
    public void listenToFinishPoll(String pollId) {
        try {
            finishPoll(Long.valueOf(pollId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}