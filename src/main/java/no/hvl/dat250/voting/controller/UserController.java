package no.hvl.dat250.voting.controller;

import lombok.extern.log4j.Log4j2;
import no.hvl.dat250.voting.DTO.PollDTO;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.util.List;

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
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO, @RequestParam(required = false) Long tempId) {
        User user = convertToEntity(userDTO);
        ResponseEntity<?> userCreationResponse = userService.createUser(user, tempId);

        if (userCreationResponse == null || !userCreationResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User creation failed");
        }

        // Cast the body to UserDTO
        UserDTO createdUserDto = (UserDTO) userCreationResponse.getBody();

        // Generate the token with the username and role from UserDTO
        String token = jwtUtils.generateToken(createdUserDto.getUsername(), createdUserDto.getRole().name());

        log.info("JWT Token generated: {}", token);

        // Create a new AuthResponse with the token and UserDTO
        AuthResponse authResponse = new AuthResponse(token, createdUserDto);

        // Return the AuthResponse
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO loginDto) {
        // Perform authentication
        Authentication requestToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(requestToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Extract that single role
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // Generate the token with the username and role
        String token = jwtUtils.generateToken(loginDto.getUsername(), role);

        // Fetch the user details to return with the token
        UserDTO userDetails = userService.findUserByUsername(loginDto.getUsername());

        return ResponseEntity.ok(new AuthResponse(token, userDetails));

    }


    // Helper method to convert DTO to entity
    private User convertToEntity(UserDTO registerDto) {
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
