package no.hvl.dat250.voting.DTO;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.hvl.dat250.voting.models.Group;
import no.hvl.dat250.voting.models.Poll;
import no.hvl.dat250.voting.models.Roles;
import no.hvl.dat250.voting.models.User;
import no.hvl.dat250.voting.models.Vote;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private Roles role;
    private List<Long> createdPollsIds;
    private List<Long> participatedPollsIds;
    private List<Long> votesIds;
    private List<Long> groupsIds;

    public static UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setCreatedPollsIds(user.getCreatedPolls().stream().map(Poll::getId).collect(Collectors.toList()));
        dto.setParticipatedPollsIds(user.getParticipatedPolls().stream().map(Poll::getId).collect(Collectors.toList()));
        dto.setVotesIds(user.getVotes().stream().map(Vote::getVoteId).collect(Collectors.toList()));
        dto.setGroupsIds(user.getGroups().stream().map(Group::getGroupId).collect(Collectors.toList()));
        return dto;
    }

    public static List<UserDTO> convertToListOfDTO(List<User> users) {
        return users.stream().map(UserDTO::convertToDTO).collect(Collectors.toList());
    }
}