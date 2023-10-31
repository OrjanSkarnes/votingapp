package no.hvl.dat250.voting.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.hvl.dat250.voting.Vote;

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

    public static VoteDTO convertToDTO(Vote vote) {
        VoteDTO dto = new VoteDTO();
        dto.setVoteId(vote.getVoteId());
        dto.setChoice(vote.getChoice());
        dto.setUserId(vote.getUser() != null ? vote.getUser().getId() : null);
        dto.setPollId(vote.getPoll().getId());
        return dto;
    }

    public static List<VoteDTO> convertToListOfDTO(List<Vote> votes) {
        return votes.stream().map(VoteDTO::convertToDTO).collect(Collectors.toList());
    }
}
