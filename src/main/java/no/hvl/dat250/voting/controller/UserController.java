package no.hvl.dat250.voting.controller;

import lombok.extern.log4j.Log4j2;
import no.hvl.dat250.voting.DTO.LoginDTO;
import no.hvl.dat250.voting.DTO.PollDTO;
import no.hvl.dat250.voting.DTO.RegisterDTO;
import no.hvl.dat250.voting.DTO.UserDTO;
import no.hvl.dat250.voting.models.Poll;
import no.hvl.dat250.voting.models.Roles;
import no.hvl.dat250.voting.models.User;
import no.hvl.dat250.voting.security.AuthResponse;
import no.hvl.dat250.voting.security.JwtUtils;
import no.hvl.dat250.voting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Log4j2
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsService userDetailsService;


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody RegisterDTO registerDto, @RequestParam(required = false) Long tempId) {
        User user = convertToEntity(registerDto);
        ResponseEntity<?> userCreationResponse = userService.createUser(user, tempId);

        if (!userCreationResponse.getStatusCode().is2xxSuccessful()) {
            return userCreationResponse;
        }

        // Cast the body to UserDTO
        UserDTO createdUserDto = (UserDTO) userCreationResponse.getBody();

        // Generate the token with the username and role from UserDTO
        // Assuming that the role is stored as a String in UserDTO
        String token = jwtUtils.generateToken(createdUserDto.getUsername(), createdUserDto.getRole().name());

        // Create a new AuthResponse with the token and UserDTO
        AuthResponse authResponse = new AuthResponse(token, createdUserDto);

        // Return the AuthResponse
        return ResponseEntity.ok(authResponse);
    }
    /*@PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDto) {
        log.info("Attempting to log in user: {}", loginDto.getUsername());
        try {
            // Perform authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );
            log.debug("Authentication token created: {}", authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Authentication successful for user: {}", authentication.getName());

            // Assuming the user has only one role, extract that single role
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            log.debug("Granted authorities: {}", role);

            // Generate the token with the username and role
            String token = jwtUtils.generateToken(loginDto.getUsername(), role);
            log.info("JWT Token generated: {}", token);

            // Fetch the user details to return with the token
            UserDTO userDetails = userService.findUserByUsername(loginDto.getUsername());
            log.debug("User details loaded: {}", userDetails);

            return ResponseEntity.ok(new AuthResponse(token, userDetails));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }*/
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDto) {
        log.info("Attempting to log in user: {}", loginDto.getUsername());
        // Perform authentication
        Authentication requestToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        );

        log.debug("Authentication request token created: {}", requestToken);
        Authentication authentication = authenticationManager.authenticate(requestToken);

        log.info("Authentication token created: {}", authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authentication successful for user: {}", authentication.getName());

        // Assuming the user has only one role, extract that single role
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        log.info("Granted authorities: {}", role);

        // Generate the token with the username and role
        String token = jwtUtils.generateToken(loginDto.getUsername(), role);
        log.error("JWT Token generated: {}", token);

        // Fetch the user details to return with the token
        UserDTO userDetails = userService.findUserByUsername(loginDto.getUsername());
        log.debug("User details loaded: {}", userDetails);

        return ResponseEntity.ok(new AuthResponse(token, userDetails));

    }


    // Helper method to convert DTO to entity
    private User convertToEntity(RegisterDTO registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setRole(Roles.ROLE_USER);
        return user;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
    public UserDTO findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody User newUser) {
        // Additional logic to handle updating a user might go here.
        return userService.updateUser(newUser);
    }

    @GetMapping("/{id}/polls")
    public List<Poll> getPollsByUser(@PathVariable Long id) {
        return userService.getPollsByUser(id);
    }

   @GetMapping("/{id}/votes/polls")
    public List<PollDTO> getPollsBasedOnVotesFromUser(@PathVariable Long id) {
        return userService.getPollsBasedOnVotesFromUser(id);
    }


}
