package com.ibm.demo.service;

import com.ibm.demo.dto.CustomerDto;
import com.ibm.demo.dto.ItemDto;
import com.ibm.demo.exception.CustomerNotFoundException;
import com.ibm.demo.exception.ErrorDetails;
import com.ibm.demo.exception.ItemNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderCircuitBreakerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCircuitBreakerService.class);

    @Autowired
    private WebClient webClient;

    @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultItem")
    public String getItemByName(String name){
        LOGGER.info("name is {} ",name);

        ItemDto itemDto = webClient.get().uri("http://localhost:8082/api/item/items/"+name)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(),error -> Mono.error(new ItemNotFoundException("Item with","name ",name)))
                .bodyToMono(ItemDto.class)
                .block();
        //Optional<ItemDto> ops = Optional.of()
        String ss = Optional.of(itemDto.getName()).orElseThrow(  ()-> new ItemNotFoundException("Item ","name",itemDto.getName()));
        return ss;
    }
    public String getDefaultItem(String name,Exception exception) {

        if(exception instanceof ItemNotFoundException){
            List<ItemDto> itemDtoList = new ArrayList<>();
            ItemDto itemDto = new ItemDto();
            itemDto.setName("Item with name "+name+" Not Available");
            return itemDto.getName();
        }else{
            ItemDto defaultItemDto = new ItemDto();
            defaultItemDto.setName("Item Service is temprory down");
            //  CustomerDto defaultCustomerDto = new CustomerDto(custId,"default first name","default last name","default@gmail.com");
            return defaultItemDto.getName();

        }



    }


    @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultCustomerId")
    public CustomerDto getCustomerById(String custId){
        LOGGER.info("custId is in getCustomerById {} ",custId);

        CustomerDto customerDto = webClient.get().uri("http://localhost:8081/api/customer/getCustomerById/"+custId)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(),error -> Mono.error(new CustomerNotFoundException("Customer with","id ",custId)))
                .bodyToMono(CustomerDto.class)
                .block();
        LOGGER.info("custId is in getCustomerById {} ",customerDto);

        return customerDto;
    }

    public CustomerDto getDefaultCustomerId(String custId,Exception exception) {
         LOGGER.info("excetion type is {} ",exception.toString());
        LOGGER.info("custId is {} ",custId);
       /* if(exception instanceof CustomerNotFoundException){
            LOGGER.info("inside if of excetion type is {} ",exception.getMessage());

            CustomerDto defaultCustomerDto = new CustomerDto();
            defaultCustomerDto.setFirstname("Customer with Custumer Id "+custId+" not available");
            return defaultCustomerDto;
        }else {*/
            CustomerDto defaultCustomerDto = new CustomerDto();
            defaultCustomerDto.setId("Customer Service is temprory down");
            defaultCustomerDto.setFirstname("defaultFirstName");
            defaultCustomerDto.setLastname("defaultLastName");
            defaultCustomerDto.setEmail("default@gmail.com");
            //  CustomerDto defaultCustomerDto = new CustomerDto(custId,"default first name","default last name","default@gmail.com");
            return defaultCustomerDto;
      //  }


    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleItemNotFoundException(ItemNotFoundException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "Item Not Found"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
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
