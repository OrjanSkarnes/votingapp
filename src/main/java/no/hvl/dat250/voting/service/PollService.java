package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.DTO.PollDTO;
import no.hvl.dat250.voting.DTO.VoteDTO;
import no.hvl.dat250.voting.models.*;
import no.hvl.dat250.voting.dao.PollDao;
import no.hvl.dat250.voting.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private LoggerService loggerService;

    @Transactional
    public PollDTO createPoll(PollDTO pollDTO) {
        Poll poll = convertToEntity(pollDTO);
        poll.setActive(true);
        setDefaultStartTime(poll);
        Poll savedPoll = pollDao.createPoll(poll);
        schedulePollEndTimeIfNecessary(savedPoll);
        return PollDTO.convertToDTO(savedPoll);
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getAllPolls() {
        return pollDao.getAllPolls().stream().map(PollDTO::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getAllPublicPolls() {
        List<Poll> polls = pollDao.getAllPublicPolls();
        return polls.stream().map(PollDTO::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PollDTO findPollById(Long id) {
        Poll poll = pollDao.findPollById(id);
        return poll != null ? PollDTO.convertToDTO(poll) : null;
    }

    @Transactional
    public void deletePoll(Long id) {
        pollDao.deletePoll(id);
    }

    @Transactional
    public PollDTO updatePoll(Long id, PollDTO pollDTO) {
        Poll pollToUpdate = pollDao.findPollById(id);
        if (pollToUpdate != null) {
            updatePollDetails(pollToUpdate, pollDTO);
            schedulePollEndTimeIfNecessary(pollToUpdate);
            return PollDTO.convertToDTO(pollDao.updatePoll(pollToUpdate));
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getPollsByUser(Long userId) {
        return pollDao.getPollsByUser(userId).stream().map(PollDTO::converteToDTOwithVotes).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getPollsCreatedByUser(Long userId) {
        return pollDao.getPollsCreatedByUser(userId).stream().map(PollDTO::converteToDTOwithVotes).collect(Collectors.toList());
    }

    @Transactional
    public List<PollDTO> getPollsBasedOnVotesFromUser(Long userId, Long tempId) {
        return pollDao.getPollsBasedOnVotesFromUser(userId, tempId).stream().map(PollDTO::converteToDTOwithVotes).collect(Collectors.toList());
    }

    @Transactional
    public List<VoteDTO> getVotesByPoll(Long id) {
        return pollDao.findPollById(id).getVotes().stream().map(VoteDTO::convertToDTO).collect(Collectors.toList());
    }

    private void setDefaultStartTime(Poll poll) {
        if (poll.getStartTime() == null) {
            poll.setStartTime(LocalDateTime.now());
        }
    }

    private void updatePollDetails(Poll poll, PollDTO pollDTO) {
        // Update the poll entity with details from the DTO
        poll.setQuestion(pollDTO.getQuestion());
        poll.setDescription(pollDTO.getDescription());
        poll.setStartTime(pollDTO.getStartTime());
        poll.setEndTime(pollDTO.getEndTime());
        poll.setActive(pollDTO.isActive());
        poll.setPrivateAccess(pollDTO.isPrivateAccess());
    }

    private void schedulePollEndTimeIfNecessary(Poll poll) {
        if (poll.getEndTime() != null) {
            EndTimeInfo endTimeInfo = new EndTimeInfo(poll.getId(), poll.getEndTime());
            kafkaProducer.sendObject(pollStreamTopic, endTimeInfo);
        }
    }

    public void finishPoll(Long id) {
        Poll poll = pollDao.findPollById(id);
        if(poll != null) {
            poll.setActive(false);
            pollDao.updatePoll(poll);
            poll.setVotesFor(poll.getVotes().stream().filter(Vote::getChoice).count());
            poll.setVotesAgainst(poll.getVotes().stream().filter(vote -> !vote.getChoice()).count());
            loggerService.log("Sending poll results to kafka");
            kafkaProducer.sendObject("pollResults", poll);
        }
    }

    @Transactional
    @KafkaListener(topics = "pollEndTimesFinished")
    public void listenToFinishPoll(String pollId) {
        loggerService.log("Poll end time finished, finishing poll" + pollId);
        finishPoll(Long.valueOf(pollId));
    }

    private Poll convertToEntity(PollDTO pollDTO) {
        Poll poll = new Poll();
        poll.setQuestion(pollDTO.getQuestion());
        poll.setDescription(pollDTO.getDescription());
        poll.setStartTime(pollDTO.getStartTime());
        poll.setEndTime(pollDTO.getEndTime());
        poll.setActive(pollDTO.isActive());
        poll.setPrivateAccess(pollDTO.isPrivateAccess());
        poll.setCreator(userDao.findUserById(pollDTO.getCreatorId()));
        poll.setUsers(userDao.findUsersByIds(pollDTO.getUserIds()));
        //poll.setGroups(userDao.findGroupsByIds(pollDTO.getGroupIds()));
        return poll;
    }
}