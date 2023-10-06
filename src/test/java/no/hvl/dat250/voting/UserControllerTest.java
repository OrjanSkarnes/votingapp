package no.hvl.dat250.voting;

import no.hvl.dat250.voting.User;
import no.hvl.dat250.voting.controller.UserController;
import no.hvl.dat250.voting.dao.UserDao;
import no.hvl.dat250.voting.driver.VotingMain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = VotingMain.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDao userDao;

    private User user;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        // ... other initializations
    }

    @Test
    public void getUser_ReturnsUser() throws Exception {
        when(userDao.findUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    // ... Additional test cases
}
