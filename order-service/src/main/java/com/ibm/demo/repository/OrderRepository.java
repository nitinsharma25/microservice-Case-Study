package com.ibm.demo.repository;

import com.ibm.demo.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
  //  List<Customer> findByFirstname(String first_name);
}
