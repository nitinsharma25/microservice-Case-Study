package com.ibm.demo.controller;

import com.ibm.demo.dto.OrderDto;
import com.ibm.demo.exception.CustomerNotFoundException;
import com.ibm.demo.exception.ErrorDetails;
import com.ibm.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Optional;


@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;



    @PostMapping("/create")
    public OrderDto createCustomer(@RequestBody OrderDto orderDto){
        return orderService.createOrder(orderDto);
    }

    @GetMapping("{id}")
    public String getCustomerById(@PathVariable("id") String custId){
        String customerId = orderService.getCustomerById(custId);
        return customerId;
    }

    @GetMapping("/getByItemName/{name}")
    public String getItemByName(@PathVariable("name") String name){
        //  APIResponseDto apiResponseDto = employeeService.getEmployeeById(employeeId);
        // return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
        String itemName = orderService.getItemByName(name);
        return itemName;
    }



        @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleCustomerNotFoundException(CustomerNotFoundException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "Customer Not Found"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
