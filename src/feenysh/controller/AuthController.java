package feenysh.controller;

import feenysh.dto.AuthResponse;
import feenysh.dto.LoginRequest;
import feenysh.dto.MessageResponse;
import feenysh.dto.RegisterRequest;
import feenysh.model.User;
import feenysh.service.AuthService;
import feenysh.security.JwtTokenProvider; // Added to generate token from User if needed

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling user authentication requests, such as registration and login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // @Autowired
    // private JwtTokenProvider tokenProvider; // Not strictly needed here if AuthService handles token or AuthResponse creation

    /**
     * Registers a new user.
     * @param registerRequest The registration request DTO containing user details.
     * @return A {@link ResponseEntity} with a success message or an error message.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User registeredUser = authService.registerUser(registerRequest);
            // Option 1: Log the user in immediately and return a token
            // String jwt = tokenProvider.generateToken(registeredUser);
            // return ResponseEntity.ok(new AuthResponse(jwt, registeredUser.getId(), registeredUser.getUsername()));

            // Option 2: Just confirm registration and require separate login
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new MessageResponse("User registered successfully! Please login."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Authenticates an existing user and returns a JWT upon successful login.
     * @param loginRequest The login request DTO containing user credentials.
     * @return A {@link ResponseEntity} containing an {@link AuthResponse} with the JWT,
     *         or an error message if authentication fails.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String jwt = authService.authenticateUser(loginRequest);
            // We need user ID and username for the AuthResponse.
            // The JWT subject is the username. We can retrieve the user from DB or parse JWT if it contains ID.
            // For simplicity, let's assume CustomUserDetailsService loads the User object that can be retrieved
            // or we fetch the user after successful authentication.
            // For now, let's assume the username is the subject and we need to fetch the user.
            // This is not ideal, usually the UserDetails from Authentication object would be used.
            // Let's refine this if UserDetails in CustomUserDetailsService is User model directly or UserPrincipal.

            // A better way: JwtTokenProvider could also create a token that includes userId as a claim.
            // Or, the Authentication object principal could be cast to your User model if it implements UserDetails.

            // For now, let's assume the token is generated from username, and we fetch user details for the response.
            // This is a common pattern if the UserDetails object itself is lean.
            org.springframework.security.core.Authentication authentication =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

            // The principal is now our feenysh.model.User object
            User userPrincipal = (User) authentication.getPrincipal();

            return ResponseEntity.ok(new AuthResponse(jwt, userPrincipal.getId(), userPrincipal.getUsername()));
        } catch (org.springframework.security.core.AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(new MessageResponse("Login failed: Invalid username or password."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new MessageResponse("An error occurred during login: " + e.getMessage()));
        }
    }
}
