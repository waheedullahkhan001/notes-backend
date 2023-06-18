package com.cloud.notesbackend.exceptions;

import com.cloud.notesbackend.responses.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {BadRequestException.class})
    protected ResponseEntity<BasicResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().body(new BasicResponse(false, e.getMessage()));
    }

}
