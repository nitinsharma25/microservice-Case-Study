package com.ibm.demo.service;

import com.ibm.demo.entity.OrderLineItem;
import com.ibm.demo.dto.CustomerDto;
import com.ibm.demo.dto.ItemDto;
import com.ibm.demo.dto.OrderDto;
import com.ibm.demo.entity.Order;
import com.ibm.demo.exception.CustomerNotFoundException;
import com.ibm.demo.exception.ErrorDetails;
import com.ibm.demo.exception.ItemNotFoundException;
import com.ibm.demo.mapper.OrderMapper;
import com.ibm.demo.repository.OrderLineItemRepository;
import com.ibm.demo.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);


    @Value("${spring.kafka.topic.name}")
    private String topicJsonName;


    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderLineItemRepository orderLineItemRepository;


    @Autowired
    private WebClient webClient;

    @Autowired
    private KafkaTemplate<String, OrderDto> kafkaTemplate;



    public OrderDto createOrder(OrderDto orderDto){
        getCustomerById(orderDto.getCustid());
        Order toSaveOrder = OrderMapper.mapToOrder(orderDto);
        Order savedOrder = orderRepository.save(toSaveOrder);
        String order_id = savedOrder.getId();
        List<String> items = orderDto.getItemList();
        int itemCount = 0;
        String itemNamestoSave="";
        String itemsNames ="";
        for (String item:items){
          String itemName = getItemByName(item);
            if(itemName.equalsIgnoreCase(item)) {
                itemsNames = itemsNames + "," + item;
                itemCount++;
            }


        }
        LOGGER.info("itemNamestoSave {} ",itemsNames);
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setItemname(itemsNames.replaceFirst(",",""));
        orderLineItem.setOrderid(order_id);
        orderLineItem.setItemquantity(itemCount);
        orderLineItemRepository.save(orderLineItem);
        OrderDto savedOrderDto = OrderMapper.mapToOrderDto(savedOrder);
        kafkaTemplate.send(topicJsonName,savedOrderDto);

        LOGGER.info(String.format("Message sent -> %s",savedOrderDto));

        return savedOrderDto;
    }



   /* @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultItem")
    public String getItemByName(String name){
        LOGGER.info("name is {} ",name);

        List<ItemDto> itemDtoList = webClient.get().uri("http://localhost:8082/api/item/items/"+name)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(),error -> Mono.error(new ItemNotFoundException("Item with","name ",name)))
                .bodyToMono(new ParameterizedTypeReference<List<ItemDto>>() {})
                .block();

        return itemDtoList.get(0).getName();
    }
    public String getDefaultItem(String name,Exception exception) {

        if(exception instanceof IndexOutOfBoundsException){
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
*/

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

        if(exception instanceof IndexOutOfBoundsException){
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
    public String getCustomerById(String custId){
        CustomerDto customerDto = webClient.get().uri("http://localhost:8081/api/customer/getCustomerById/"+custId)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(),error -> Mono.error(new CustomerNotFoundException("Customer with","id ",custId)))
                .bodyToMono(CustomerDto.class)
                .block();
        return customerDto.getId();
    }

    public String getDefaultCustomerId(String custId,Exception exception) {
        LOGGER.info("excetion type is {} ",exception);
        LOGGER.info("custId is {} ",custId);

        if(exception instanceof CustomerNotFoundException){
                    CustomerDto defaultCustomerDto = new CustomerDto();
                    defaultCustomerDto.setId("Customer with id "+custId+" not available");
                    return defaultCustomerDto.getId();
        }else {
                    CustomerDto defaultCustomerDto = new CustomerDto();
                    defaultCustomerDto.setId("Customer Service is temprory down");
                    defaultCustomerDto.setFirstname("defaultFirstName");
                    defaultCustomerDto.setLastname("defaultLastName");
                    defaultCustomerDto.setEmail("default@gmail.com");
                    //  CustomerDto defaultCustomerDto = new CustomerDto(custId,"default first name","default last name","default@gmail.com");
                    return defaultCustomerDto.getId();
        }

    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleCustomerNotFoundException(ItemNotFoundException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "Item Not Found"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
