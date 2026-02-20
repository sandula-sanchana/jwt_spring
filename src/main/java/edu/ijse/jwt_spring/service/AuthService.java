package edu.ijse.jwt_spring.service;

import edu.ijse.jwt_spring.dto.RegisterDTO;
import edu.ijse.jwt_spring.entity.Role;
import edu.ijse.jwt_spring.entity.User;
import edu.ijse.jwt_spring.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public String register(RegisterDTO registerDTO){

        User user=User.builder().username(registerDTO.getUsername()).
                password(passwordEncoder.encode(registerDTO.getPassword())).
                role(Role.valueOf(registerDTO.getRole())).build();

        userRepo.save(user);

        return "user "+user.getUsername() + " saved successfully";

    }
}
