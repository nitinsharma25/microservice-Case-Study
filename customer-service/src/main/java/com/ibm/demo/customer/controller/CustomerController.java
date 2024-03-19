package com.ibm.demo.customer.controller;

import com.ibm.demo.customer.dto.CustomerDto;
import com.ibm.demo.customer.entity.Customer;
import com.ibm.demo.customer.exception.CustomerNotFoundException;
import com.ibm.demo.customer.exception.ErrorDetails;
import com.ibm.demo.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/create")
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto){
        return customerService.createCustomer(customerDto);
    }

    @GetMapping("/getCustomerById/{id}")
    public CustomerDto getCustomerById(@PathVariable String id){
        return customerService.getCustomerById(id);
    }

    @GetMapping("/customers")
    public List<CustomerDto> getAllCustomer(){
        return customerService.getAllCustomer();
    }
    @PutMapping("/updateCustomerById")
    public CustomerDto updateCustomerById(@RequestBody CustomerDto customerDto){
        return customerService.updateCustomer(customerDto);
    }

    @DeleteMapping("/deleteCustomerById/{id}")
    public String deleteCustomerById(@PathVariable String id){
         customerService.deleteCustomerById(id);
         return "delete Success";
    }
    @GetMapping("/customers/{name}")
    public List<CustomerDto> findCustomerById(@PathVariable String name){
      return   customerService.findByCustomerName(name);
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
