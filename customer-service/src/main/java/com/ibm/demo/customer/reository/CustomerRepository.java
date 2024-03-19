package com.ibm.demo.customer.reository;

import com.ibm.demo.customer.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,String> {
    List<Customer> findByFirstname(String first_name);
}
