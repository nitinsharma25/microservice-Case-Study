package com.ibm.demo.repository;

import com.ibm.demo.entity.OrderLineItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemRepository extends MongoRepository<OrderLineItem,String> {
}
