package edu.ijse.jwt_spring.service;

import edu.ijse.jwt_spring.dto.AuthDTO;
import edu.ijse.jwt_spring.dto.AuthResponseDTO;
import edu.ijse.jwt_spring.dto.RegisterDTO;
import edu.ijse.jwt_spring.entity.Role;
import edu.ijse.jwt_spring.entity.User;
import edu.ijse.jwt_spring.repo.UserRepo;
import edu.ijse.jwt_spring.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    public String register(RegisterDTO registerDTO) {

        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(Role.valueOf(registerDTO.getRole()))
                .build();

        userRepo.save(user);

        return "user " + user.getUsername() + " saved successfully";
    }

    public AuthResponseDTO authenticate(AuthDTO authDTO) {

        User user = userRepo.findByUsername(authDTO.getUsername())
                .orElseThrow(() -> new BadCredentialsException("incorrect username or password"));

        if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("incorrect username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponseDTO(token);
    }
}