package edu.ijse.jwt_spring.controller;

import edu.ijse.jwt_spring.dto.AuthDTO;
import edu.ijse.jwt_spring.dto.RegisterDTO;
import edu.ijse.jwt_spring.service.AuthService;
import edu.ijse.jwt_spring.util.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

  @PostMapping("register")
  @ResponseStatus(HttpStatus.OK)
  public APIResponse<Object> register(@RequestBody RegisterDTO registerDTO) {
     return new APIResponse<>(
              200,
              "Register Successfully",
              authService.register(registerDTO)
      );

  }

    @PostMapping("login")
    public ResponseEntity<APIResponse<Object>> loginUser(@RequestBody AuthDTO authDTO){
        return ResponseEntity.ok(new APIResponse<>(
                200,"OK",
                authService.authenticate(authDTO)
        ));
    }


}
