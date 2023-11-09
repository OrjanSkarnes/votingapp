package no.hvl.dat250.voting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserAuthenticationTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testUserAuthentication() {
        // Arrange
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String username = "testuser";

        // Normally, you'd save the encoded password in the database.
        // For this test, let's just print it out and use it directly.
        System.out.println("Encoded Password: " + encodedPassword);

        // Act
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, rawPassword);

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // Assert
            assertTrue(authentication.isAuthenticated(), "The authentication should be successful");

            // If you want to check the password matches manually, you can do so as well:
            assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "The passwords should match");

        } catch (AuthenticationException e) {
            // Assert
            fail("Authentication failed with message: " + e.getMessage());
        }
    }
}
