package com.twitter.demo.exception_handler

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    def handleException(Exception e) {
        return ResponseEntity.badRequest().body("Something went wrong: " + e.getMessage())
    }
}
