package no.hvl.dat250.voting;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat250.voting.controller.VoteController;
import no.hvl.dat250.voting.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

    @Test
    public void createVote_ReturnsCreatedVote() throws Exception {
        Vote newVote = new Vote();
        newVote.setChoice(Boolean.TRUE);

        when(voteService.createVote(any(Vote.class))).thenReturn(newVote);

        mockMvc.perform(post("/api/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newVote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.choice", is(newVote.getChoice())));
    }

    @Test
    public void getAllVotes_ReturnsAllVotes() throws Exception {
        List<Vote> votes = Arrays.asList(new Vote(), new Vote());

        when(voteService.getAllVotes()).thenReturn(votes);

        mockMvc.perform(get("/api/votes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(votes.size())));
    }

    @Test
    public void findVoteById_ReturnsVote() throws Exception {
        Vote vote = new Vote();
        vote.setChoice(Boolean.TRUE);

        when(voteService.findVoteById(1L)).thenReturn(vote);

        mockMvc.perform(get("/api/votes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.choice", is(vote.getChoice())));
    }
}
