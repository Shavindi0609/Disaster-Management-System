package com.ijse.gdse.back_end.exception;

import com.ijse.gdse.back_end.dto.APIResponse;
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
    public APIResponse handleUserNameNotFoundException
            (UsernameNotFoundException ex) {
        return new APIResponse(404,"User Not Found", null);
    }


    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handleCredentials(BadCredentialsException ex) {
        return new APIResponse(400,"Bad Credentials", null);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse handleAllExceptions(RuntimeException ex) {
        return new APIResponse(500,"Internal Server Error", null);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handleInsufficientFunds(InsufficientFundsException ex) {
        return new APIResponse(400, ex.getMessage(), null);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIResponse handleUnauthorizedAction(UnauthorizedActionException ex) {
        return new APIResponse(403, ex.getMessage(), null);
    }
}
