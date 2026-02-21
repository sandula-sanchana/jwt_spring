package edu.ijse.jwt_spring.service;

import edu.ijse.jwt_spring.dto.AuthDTO;
import edu.ijse.jwt_spring.dto.AuthResponseDTO;
import edu.ijse.jwt_spring.dto.RegisterDTO;
import edu.ijse.jwt_spring.entity.Role;
import edu.ijse.jwt_spring.entity.User;
import edu.ijse.jwt_spring.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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

    public AuthResponseDTO autheticate(AuthDTO authDTO){

        User user=userRepo.findByUsername(authDTO.getUsername());

        if(!passwordEncoder.matches(authDTO.getPassword(),user.getPassword())){
            throw new BadCredentialsException("incorrect username or password");
        }



    }
}
