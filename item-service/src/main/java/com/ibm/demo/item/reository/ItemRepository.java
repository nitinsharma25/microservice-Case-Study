package com.ibm.demo.item.reository;

import com.ibm.demo.item.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends MongoRepository<Item,String> {
   // List<Item> findByName(String name);
    Optional<Item> findByName(String name);
}
