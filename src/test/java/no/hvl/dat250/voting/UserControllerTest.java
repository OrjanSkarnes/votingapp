package no.hvl.dat250.voting;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat250.voting.dao.UserDao;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock the UserService if it contains additional logic
    @MockBean
    private UserDao userDao;

    private User user;

    @BeforeEach
    public void setUp() {
        this.user = new User();
        this.user.setId(1L);
        this.user.setUsername("testUser");
        // ... other initializations
    }

    @Test
    public void getUser_ReturnsUser() throws Exception {
        when(userDao.findUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))  // Note the URL change
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    public void createUser_ReturnsCreatedUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("newUser");

        when(userDao.createUser(any(User.class))).thenReturn(newUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(jsonPath("$.username", is(newUser.getUsername())));
    }


    @Test
    public void getAllUsers_ReturnsAllUsers() throws Exception {
        List<User> users = Arrays.asList(new User(), new User());

        when(userDao.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(users.size())));
    }

    @Test
    public void deleteUser_DeletesUser() throws Exception {
        User existingUser = new User();
        existingUser.setId(1L);

        when(userDao.findUserById(1L)).thenReturn(existingUser);
        doNothing().when(userDao).deleteUser(existingUser);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().is(200));
    }

    @Test
    public void updateUser_UpdatesUser() throws Exception {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existingUser");

        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");

        when(userDao.updateUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(updatedUser.getUsername())));
    }
}
