package no.hvl.dat250.voting;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat250.voting.controller.GroupController;
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
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock the UserService if it contains additional logic
    @MockBean
    private GroupController groupController;


    @Test
    public void createGroup_ReturnsCreatedGroup() throws Exception {
        Group newGroup = new Group();
        newGroup.setName("newGroup");

        when(groupController.createGroup(any(Group.class))).thenReturn(newGroup);

        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newGroup)))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name", is(newGroup.getName())));
    }

    @Test
    public void getAllGroups_ReturnsAllGroups() throws Exception {
        List<Group> groups = Arrays.asList(new Group(), new Group());

        when(groupController.getAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(groups.size())));
    }


    @Test
    public void deleteGroup_DeletesGroup() throws Exception {

        doNothing().when(groupController).deleteGroup(1L);

        mockMvc.perform(delete("/api/groups/1"))
                .andExpect(status().is(200));
    }

    @Test
    public void updateGroup_UpdatesGroup() throws Exception {
        Group updatedGroup = new Group();
        updatedGroup.setName("updatedGroup");

        when(groupController.updateGroup(any(Long.class), any(Group.class))).thenReturn(updatedGroup);

        mockMvc.perform(put("/api/groups/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedGroup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedGroup.getName())));
    }

}
