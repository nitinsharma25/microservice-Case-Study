package com.ibm.demo.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/*@EnableHystrix
@EnableHystrixDashboard*/


@SpringBootApplication
@EnableMongoRepositories("com.ibm.demo.customer.reository")

public class CustomerServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

}
