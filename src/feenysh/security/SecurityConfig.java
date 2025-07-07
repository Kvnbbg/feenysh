package feenysh.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // Enables @Secured and @RolesAllowed
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable()) // Add appropriate CORS configuration if frontend is on a different origin
            .csrf(csrf -> csrf.disable()) // Disable CSRF as we are using JWT
            .exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
            )
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session for JWT
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    new AntPathRequestMatcher("/api/auth/**"),
                    new AntPathRequestMatcher("/h2-console/**"), // Allow H2 console access
                    new AntPathRequestMatcher("/ws/**") // Allow WebSocket connections (further security might be needed at WebSocket level)
                ).permitAll()
                .anyRequest().authenticated() // All other requests require authentication
            );

        // Add JWT token filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers(headers -> headers
            // X-Frame-Options: SAMEORIGIN allows framing by pages on the same origin.
            // This is a common setting that works with H2 console.
            .frameOptions(frameOptions -> frameOptions.sameOrigin())
            // X-Content-Type-Options: nosniff
            .contentTypeOptions(contentTypeOptionsConfig -> {})
            // Content-Security-Policy: A very basic policy.
            // For a real application, this needs careful crafting.
            // 'self' for scripts and default, 'none' for objects.
            // frame-ancestors 'self' is important for clickjacking protection.
            .contentSecurityPolicy(csp -> csp
                .policyDirectives("default-src 'self'; script-src 'self'; object-src 'none'; frame-ancestors 'self'; form-action 'self';")
            )
            // X-XSS-Protection header is deprecated and its use is not recommended. CSP is preferred.
            // .xssProtection(...)
            // Strict-Transport-Security: Only send if HTTPS is configured and enforced.
            // .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000))
        );

        return http.build();
    }
}
