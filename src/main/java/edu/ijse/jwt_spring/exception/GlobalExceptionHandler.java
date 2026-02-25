package edu.ijse.jwt_spring.exception;

import edu.ijse.jwt_spring.util.APIResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse<String> handleUserNameNotFound( UsernameNotFoundException  usernameNotFoundException) {
       return new APIResponse<>(
               HttpStatus.NOT_FOUND.value(),
               "username or password is incorrect",
               usernameNotFoundException.getMessage()
       );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse<String> handleBadCredentials( BadCredentialsException bc) {
        return new APIResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                "username or password is incorrect",
                bc.getMessage()
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse<String> handleExpiredJwtException(ExpiredJwtException expiredJwtException) {
        return new APIResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                "expired token",
                expiredJwtException.getMessage()
        );
    }



}
