package com.ibm.demo.customer.service;

import com.ibm.demo.customer.dto.CustomerDto;
import com.ibm.demo.customer.entity.Customer;
import com.ibm.demo.customer.exception.CustomerNotFoundException;
import com.ibm.demo.customer.exception.ErrorDetails;
import com.ibm.demo.customer.mapper.CustomerMapper;
import com.ibm.demo.customer.reository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public CustomerDto createCustomer(CustomerDto customerDto){
       Customer customer = CustomerMapper.mapToCustomer(customerDto);
       Customer savedCustomer = customerRepository.save(customer);
       CustomerDto customerDtoResponse = CustomerMapper.mapToCustomerDto(savedCustomer);
     //  return customerRepository.save(customer);
        return customerDtoResponse;
    }

    public CustomerDto getCustomerById(String id){
        Customer customer = customerRepository.findById(id).orElseThrow(
                ()-> new CustomerNotFoundException("Customer","id",id)
        );
        CustomerDto customerDtoResponse = CustomerMapper.mapToCustomerDto(customer);
        return customerDtoResponse;
    }

    public CustomerDto getCustomerByEmail(String email){
        Customer customer = customerRepository.findByEmail(email).orElseThrow(
                ()-> new CustomerNotFoundException("Customer","email",email)
        );
        CustomerDto customerDtoResponse = CustomerMapper.mapToCustomerDto(customer);
        return customerDtoResponse;
    }

    public void deleteCustomerById(String id){
         customerRepository.deleteById(id);
    }
    public CustomerDto updateCustomer(CustomerDto customerDto){
        Customer customer = CustomerMapper.mapToCustomer(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        CustomerDto customerDtoResponse = CustomerMapper.mapToCustomerDto(savedCustomer);
        return customerDtoResponse;
        //return customerRepository.save(customer);
    }

    public List<CustomerDto> getAllCustomer(){
        List<CustomerDto> customerDtoResonseList = new ArrayList<CustomerDto>();
        List<Customer> customerList = customerRepository.findAll();
        for (Customer customer:customerList){
            CustomerDto customerDtoResponse = CustomerMapper.mapToCustomerDto(customer);
            customerDtoResonseList.add(customerDtoResponse);
        }
        return customerDtoResonseList;
    }

    public List<CustomerDto> findByCustomerName(String name){
     //   return customerRepository.findByFirstname(name);
        List<CustomerDto> customerDtoResonseList = new ArrayList<CustomerDto>();
        List<Customer> customerList = customerRepository.findByFirstname(name);
        for (Customer customer:customerList){
            CustomerDto customerDtoResponse = CustomerMapper.mapToCustomerDto(customer);
            customerDtoResonseList.add(customerDtoResponse);
        }
        return customerDtoResonseList;
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
