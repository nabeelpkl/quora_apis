package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<ErrorResponse> authenticationFailedException(
      AuthenticationFailedException e, WebRequest webRequest) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(e.getCode()).message(e.getErrorMessage()),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> authenticationFailedException(
      UserNotFoundException e, WebRequest webRequest) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(e.getCode()).message(e.getErrorMessage()), HttpStatus.NOT_FOUND);
  }
}
