package com.ibm.demo.service;

// Java Program to Illustrate Creation Of
// Service implementation class

import com.ibm.demo.dto.CustomerDto;
import com.ibm.demo.dto.EmailDetails;
import com.ibm.demo.dto.OrderDto;
import com.ibm.demo.exception.CustomerNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;

// Annotation
@Service
// Class
// Implementing EmailService interface
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger("EmailServiceImpl");


    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.kafka.topic.name}")
    private String topicJsonName;

    @Autowired
    private WebClient webClient;


    // @KafkaListener(topics = "OrderCreatedNew1", groupId = "Group100",containerFactory = "animalListener")
    //createOrderTopic
    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "Group100", containerFactory = "orderListener")
    public void consume(OrderDto orderDto) {
        LOGGER.info("topic name {} ", topicJsonName);
        LOGGER.info("from topic createOrderTopic consume {} ", orderDto.getId());
        String customerId = orderDto.getCustid();
       CustomerDto returnedCustomerDto = getCustomerById(customerId);
        String bodyMsg = "Dear "+returnedCustomerDto.getFirstname()+" "+returnedCustomerDto.getLastname()+" your Order for "+orderDto.getOrderdesc()+" has been processed Successfully";

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(returnedCustomerDto.getEmail());
        emailDetails.setSubject("Order Placed Success");
        emailDetails.setMsgBody(bodyMsg);
        emailDetails.setAttachment("Thanks");

        sendSimpleMail(emailDetails);
    }

    // Method 1
    // To send a simple email
    public String sendSimpleMail(EmailDetails details) {
        // consume();

        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    // Method 2
    // To send an email with attachment
    public String
    sendMailWithAttachment(EmailDetails details) {
        // Creating a mime message
        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                    details.getSubject());

            // Adding the attachment
            FileSystemResource file
                    = new FileSystemResource(
                    new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(
                    file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        // Catch block to handle MessagingException
        catch (MessagingException e) {

            // Display message when exception occurred
            return "Error while sending mail!!!";
        }
    }

    @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultCustomer")
    public CustomerDto getCustomerById(String custId) {
        CustomerDto customerDto = webClient.get().uri("http://localhost:8081/api/customer/getCustomerById/" + custId)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(), error -> Mono.error(new CustomerNotFoundException("Customer with", "id ", custId)))
                .bodyToMono(CustomerDto.class)
                .block();
        return customerDto;
    }

    public CustomerDto getDefaultCustomer(String custId, Exception exception) {
        LOGGER.info("excetion type is {} ", exception);
        LOGGER.info("custId is {} ", custId);

        if (exception instanceof CustomerNotFoundException) {
            CustomerDto defaultCustomerDto = new CustomerDto();
            defaultCustomerDto.setId("Customer with id " + custId + " not available");
            return defaultCustomerDto;
        } else {
            CustomerDto defaultCustomerDto = new CustomerDto();
            defaultCustomerDto.setId("Customer Service is temprory down");
            defaultCustomerDto.setFirstname("defaultFirstName");
            defaultCustomerDto.setLastname("defaultLastName");
            defaultCustomerDto.setEmail("default@gmail.com");
            //  CustomerDto defaultCustomerDto = new CustomerDto(custId,"default first name","default last name","default@gmail.com");
            return defaultCustomerDto;
        }

    }
}
