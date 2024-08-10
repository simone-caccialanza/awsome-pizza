package com.simocaccia.awsomepizza.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class PizzaExceptionHandler {
    @ExceptionHandler(CheckOrderNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCheckOrderNotFoundException(CheckOrderNotFoundException e, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Order not found");
        pd.setDetail(e.getMessage());
        String requestUri = ((ServletWebRequest) request).getRequest().getRequestURI();
        pd.setInstance(URI.create(requestUri));
        pd.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.status(pd.getStatus()).body(pd);
    }
}
