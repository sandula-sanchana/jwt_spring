package edu.ijse.jwt_spring.controller;

import edu.ijse.jwt_spring.util.APIResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/hello")
@CrossOrigin
public class HelloController {

    @GetMapping
    public APIResponse<String> hello(){
        System.out.println("hello");
        return new APIResponse<>(
                200,
                "HELLO00",
                null
        );
    }

}
