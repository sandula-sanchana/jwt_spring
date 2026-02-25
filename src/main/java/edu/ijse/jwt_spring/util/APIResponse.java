package edu.ijse.jwt_spring.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIResponse<T> {

    private int status;
    private String message;
    private T data;
}
