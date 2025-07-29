package com.mayadem.battlearena.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mayadem.battlearena.api.dto.ErrorResponseDto;

@ControllerAdvice 
public class GlobalExceptionHandler {

  
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateResource(DuplicateResourceException ex) {
        
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.CONFLICT.value(),
            "Conflict",
            ex.getMessage() 
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
