package com.ibm.demo.customer.reository;

import com.ibm.demo.customer.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,String> {
    List<Customer> findByFirstname(String first_name);
    Optional<Customer> findByEmail(String email);
}
