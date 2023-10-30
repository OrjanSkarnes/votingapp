package no.hvl.dat250.voting;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat250.voting.DTO.PollDTO;
import no.hvl.dat250.voting.controller.PollController;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = VotingMain.class)
@AutoConfigureMockMvc
public class PollControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PollController pollService;

    @Test
    public void createPoll_ReturnsCreatedPoll() throws Exception {
        Poll newPoll = new Poll();
        newPoll.setQuestion("newPoll");

        when(pollService.createPoll(any(Poll.class))).thenReturn(newPoll);

        mockMvc.perform(post("/api/polls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newPoll)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question", is(newPoll.getQuestion())));
    }

    @Test
    public void getAllPolls_ReturnsAllPolls() throws Exception {
        List<Poll> polls = Arrays.asList(new Poll(), new Poll());
        List<PollDTO> pollDTOs = polls.stream().map(PollDTO::convertToDTO).collect(Collectors.toList());

        when(pollService.getAllPolls()).thenReturn(pollDTOs);

        mockMvc.perform(get("/api/polls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(polls.size())));
    }

    @Test
    public void findPollById_ReturnsPoll() throws Exception {
        Poll poll = new Poll();
        poll.setId(1L);
        poll.setQuestion("testPoll");

        PollDTO pollDTO = PollDTO.convertToDTO(poll);

        when(pollService.findPollById(1L)).thenReturn(ResponseEntity.ok(pollDTO));

        mockMvc.perform(get("/api/polls/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question", is(poll.getQuestion())));
    }

    @Test
    public void deletePoll_DeletesPoll() throws Exception {
        doNothing().when(pollService).deletePoll(1L);

        mockMvc.perform(delete("/api/polls/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePoll_UpdatesPoll() throws Exception {
        Poll updatedPoll = new Poll();
        updatedPoll.setQuestion("updatedPoll");

        PollDTO updatedPollDTO = PollDTO.convertToDTO(updatedPoll);

        when(pollService.updatePoll(eq(1L), any(Poll.class))).thenReturn(updatedPollDTO);

        mockMvc.perform(put("/api/polls/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedPoll)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question", is(updatedPoll.getQuestion())));
    }

    @Test
    public void getPollsByUser_ReturnsPolls() throws Exception {
        List<PollDTO> polls = Stream.of(new Poll(), new Poll()).map(PollDTO::convertToDTO).collect(Collectors.toList());

        when(pollService.getPollsByUser(1L)).thenReturn(polls);

        mockMvc.perform(get("/api/polls/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(polls.size())));
    }
}
