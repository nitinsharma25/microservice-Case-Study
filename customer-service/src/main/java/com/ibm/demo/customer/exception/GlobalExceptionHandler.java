package com.ibm.demo.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<CustomerNotFoundException> handleProductNotFoundException(CustomerNotFoundException ex) {
        CustomerNotFoundException errorResponse = new CustomerNotFoundException("Customer Not Found",ex.getMessage(),"");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
