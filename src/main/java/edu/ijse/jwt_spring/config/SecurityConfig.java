package edu.ijse.jwt_spring.config;

import edu.ijse.jwt_spring.util.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // allows @PreAuthorize, @Secured, etc.
public class SecurityConfig {

    // Loads users from DB (your ApplicationConfig provides this bean)
    private final UserDetailsService userDetailsService;

    // Your custom filter that reads JWT from Authorization header and sets SecurityContext
    private final JwtFilter jwtFilter;

    // BCrypt encoder bean (your ApplicationConfig provides this)
    private final PasswordEncoder passwordEncoder;

    /**
     *Main Security configuration (the "rules" + the filter chain).
     * Every incoming HTTP request passes through this chain of filters.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                /**
                 *  Disable CSRF
                 * For stateless REST APIs using JWT, CSRF is usually disabled.
                 * (CSRF protection is mainly for browser sessions with cookies.)
                 */
                .csrf(AbstractHttpConfigurer::disable)

                /**
                 * ✅ Authorization rules (who can access what)
                 */
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (login/register/refresh etc.)
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Everything else must have a valid authentication
                        .anyRequest().authenticated()
                )

                /**
                 * ✅ Stateless session (VERY IMPORTANT for JWT)
                 * Spring will NOT create an HTTP session.
                 * Every request must bring its JWT token again.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                /**
                 * ✅ Tells Spring "how to authenticate username+password"
                 * DaoAuthenticationProvider will:
                 *  1) call UserDetailsService.loadUserByUsername()
                 *  2) compare passwords using PasswordEncoder
                 */
                .authenticationProvider(authenticationProvider())

                /**
                 * ✅ Add our JWT filter BEFORE Spring's username/password auth filter.
                 * This makes sure JWT is checked first on every request:
                 *  - If token is valid -> SecurityContext is set -> request continues as authenticated
                 *  - If token missing/invalid -> request continues as unauthenticated -> blocked by rules above
                 */
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Build and return the configured filter chain
        return http.build();
    }

    /**
     * ✅ AuthenticationProvider:
     * This is the component that actually verifies username/password during login.
     *
     * DaoAuthenticationProvider uses:
     *  - UserDetailsService (to load user from DB)
     *  - PasswordEncoder (to verify raw vs hashed password)
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // 1) How to load the user (DB -> UserDetails)
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

        // 2) How to check password (raw password vs BCrypt hash in DB)
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }
}