package no.hvl.dat250.voting.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.hvl.dat250.voting.models.Vote;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class VoteDTO {
    private Long voteId;
    private Boolean choice;
    private Long userId;
    private Long pollId;
    private UserDTO user;
    private PollDTO poll;

    public static VoteDTO convertToDTO(Vote vote) {
        VoteDTO dto = new VoteDTO();
        dto.setVoteId(vote.getVoteId());
        dto.setChoice(vote.getChoice());
        dto.setUserId(vote.getUser() != null ? vote.getUser().getId() : null);
        dto.setPollId(vote.getPoll() != null ? vote.getPoll().getId() : null);
        return dto;
    }

    public static VoteDTO converteToDTOwithObjects(Vote vote) {
        VoteDTO dto = new VoteDTO();
        dto.setVoteId(vote.getVoteId());
        dto.setChoice(vote.getChoice());
        dto.setUser(vote.getUser() != null ? UserDTO.convertToDTO(vote.getUser()) : null);
        dto.setPoll(vote.getPoll() != null ? PollDTO.converteToDTOwithVotes(vote.getPoll()) : null);
        return dto;
    }

    public static List<VoteDTO> convertToListOfDTO(List<Vote> votes) {
        return votes.stream().map(VoteDTO::convertToDTO).collect(Collectors.toList());
    }
}
