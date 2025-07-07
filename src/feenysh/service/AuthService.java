package feenysh.service;

import feenysh.dto.LoginRequest;
import feenysh.dto.RegisterRequest;
import feenysh.model.User;
import feenysh.repository.UserRepository;
import feenysh.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.transaction.annotation.Transactional;


/**
 * Service class for handling authentication logic, including user registration
 * and login.
 */
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * Registers a new user in the system.
     *
     * @param registerRequest DTO containing user registration details (username, password, email, displayName).
     * @return The saved {@link User} entity.
     * @throws IllegalArgumentException if the username or email is already taken.
     */
    @Transactional
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email Address already in use!");
        }

        User user = new User(
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getEmail(),
                registerRequest.getDisplayName() != null ? registerRequest.getDisplayName() : registerRequest.getUsername()
        );

        return userRepository.save(user);
    }

    /**
     * Authenticates an existing user and generates a JWT token upon successful authentication.
     *
     * @param loginRequest DTO containing user login credentials (username, password).
     * @return A JWT token string if authentication is successful.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    public String authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate token using the UserDetails from the authentication object
        // Or, fetch the User entity if you need more info for the token claims
        // User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
        // return tokenProvider.generateToken(user);
        return tokenProvider.generateToken(authentication);
    }
}
