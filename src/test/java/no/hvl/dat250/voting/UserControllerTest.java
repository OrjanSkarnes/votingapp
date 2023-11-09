package no.hvl.dat250.voting;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat250.voting.DTO.UserDTO;
import no.hvl.dat250.voting.models.User;
import no.hvl.dat250.voting.service.UserService;
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
    private UserService userService;

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
        UserDTO userDTO = UserDTO.convertToDTO(user);
        when(userService.findUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/user/1"))  // Note the URL change
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(userDTO.getUsername())));
    }

    // @Test
    // public void createUser_ReturnsCreatedUser() throws Exception {
    //     User newUser = new User();
    //     newUser.setUsername("newUser");
    //     newUser.setPassword("password");

    //     UserDTO newUserDTO = UserDTO.convertToDTO(newUser);

    //     when(userService.createUser(newUser, 0L)).thenReturn(ResponseEntity.ok(newUserDTO));

    //     mockMvc.perform(post("/api/user/register")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(new ObjectMapper().writeValueAsString(newUserDTO)))
    //             .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
    //             .andExpect(jsonPath("$.user.username", is(newUserDTO.getUsername())));
    // }


    @Test
    public void getAllUsers_ReturnsAllUsers() throws Exception {
        List<User> users = Arrays.asList(new User(), new User());
        List<UserDTO> userDTOs = UserDTO.convertToListOfDTO(users);

        when(userService.getAllUsers()).thenReturn(userDTOs);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(userDTOs.size())));
    }

    @Test
    public void deleteUser_DeletesUser() throws Exception {
        User existingUser = new User();
        existingUser.setId(1L);

        UserDTO existingUserDTO = UserDTO.convertToDTO(existingUser);

        when(userService.findUserById(1L)).thenReturn(existingUserDTO);
        doNothing().when(userService).deleteUser(existingUserDTO.getId());

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().is(200));
    }

    @Test
    public void updateUser_UpdatesUser() throws Exception {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existingUser");

        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");

        UserDTO existingUserDTO = UserDTO.convertToDTO(existingUser);
        UserDTO updatedUserDTO = UserDTO.convertToDTO(updatedUser);

        when(userService.updateUser(any(User.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(put("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(updatedUserDTO.getUsername())));
    }
}
