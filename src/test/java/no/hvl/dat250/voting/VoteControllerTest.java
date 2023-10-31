package no.hvl.dat250.voting;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat250.voting.DTO.VoteDTO;
import no.hvl.dat250.voting.controller.VoteController;
import no.hvl.dat250.voting.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;


@SpringBootTest(classes = VotingMain.class)
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoteService voteService;

    private User testUser = new User();

    private Poll testPoll = new Poll();

    @Test
    public void createVote_ReturnsCreatedVote() throws Exception {
        Vote newVote = voteCreator(true);

        when(voteService.createVote(any(VoteDTO.class), any())).thenReturn(ResponseEntity.ok(VoteDTO.convertToDTO(newVote)));

        mockMvc.perform(post("/api/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newVote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.choice", is(newVote.getChoice())));
    }

    @Test
    public void getAllVotes_ReturnsAllVotes() throws Exception {
        List<Vote> votes = Arrays.asList(voteCreator(true), voteCreator(true));

        System.out.println(voteService.getAllVotes());

        when(voteService.getAllVotes()).thenReturn(VoteDTO.convertToListOfDTO(votes));


        mockMvc.perform(get("/api/votes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(votes.size())));
    }

    @Test
    public void findVoteById_ReturnsVote() throws Exception {
        Vote vote = voteCreator(true);

        when(voteService.findVoteById(1L)).thenReturn(VoteDTO.convertToDTO(vote));

        mockMvc.perform(get("/api/votes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.choice", is(vote.getChoice())));
    }

    public Vote voteCreator(Boolean choice) {
        Vote vote = new Vote();
        vote.setChoice(choice);
        vote.setUser(testUser);
        vote.setPoll(testPoll);
        return vote;
    }
}
