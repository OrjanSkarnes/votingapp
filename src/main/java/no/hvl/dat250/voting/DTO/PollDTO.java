package no.hvl.dat250.voting.DTO;

import lombok.*;
import no.hvl.dat250.voting.Group;
import no.hvl.dat250.voting.Poll;
import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.Vote;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor // Lombok annotation for no-args constructor
@AllArgsConstructor 
public class PollDTO {
    private Long id;
    private String question;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean active;
    private boolean privateAccess;
    private List<VoteDTO> votes;
    private List<Long> voteIds;
    private List<Long> groupId;
    private List<Long> userIds;
    private Long creatorId;

    public static PollDTO convertToDTO(Poll poll) {
        return PollDTO.builder()
                .id(poll.getId())
                .question(poll.getQuestion())
                .description(poll.getDescription())
                .startTime(poll.getStartTime())
                .endTime(poll.getEndTime())
                .active(poll.isActive())
                .privateAccess(poll.isPrivateAccess())
                .voteIds(poll.getVotes().stream().map(Vote::getVoteId).collect(Collectors.toList()))
                .groupId(poll.getGroups().stream().map(Group::getGroupId).collect(Collectors.toList()))
                .userIds(poll.getUsers().stream().map(User::getId).collect(Collectors.toList()))
                .creatorId(poll.getCreator().getId())
                .build();
    }

    public static PollDTO converteToDTOwithVotes(Poll poll) {
        return PollDTO.builder()
                .id(poll.getId())
                .question(poll.getQuestion())
                .description(poll.getDescription())
                .startTime(poll.getStartTime())
                .endTime(poll.getEndTime())
                .active(poll.isActive())
                .privateAccess(poll.isPrivateAccess())
                .votes(poll.getVotes().stream().map(VoteDTO::convertToDTO).collect(Collectors.toList()))
                .groupId(poll.getGroups().stream().map(Group::getGroupId).collect(Collectors.toList()))
                .userIds(poll.getUsers().stream().map(User::getId).collect(Collectors.toList()))
                .creatorId(poll.getCreator().getId())
                .build();
    }

    public static List<PollDTO> convertToListOfDTO(List<Poll> polls) {
        return polls.stream().map(PollDTO::convertToDTO).collect(Collectors.toList());
    }
}
