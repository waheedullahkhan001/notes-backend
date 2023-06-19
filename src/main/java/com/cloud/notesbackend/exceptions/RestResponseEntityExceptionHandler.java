package com.cloud.notesbackend.exceptions;

import com.cloud.notesbackend.responses.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {


    @ExceptionHandler(value = {BadRequestException.class})
    protected ResponseEntity<BasicResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().body(new BasicResponse(false, e.getMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<BasicResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(new BasicResponse(false, e.getAllErrors().get(0).getDefaultMessage()));
    }

}
