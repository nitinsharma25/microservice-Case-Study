package com.ibm.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String id;
    @Field("order_date")
    private String orderdate;
    @Field("order_desc")
    private String orderdesc;
    @Field("cust_id")
    private String custid;
    @Field("total_price")
    private Double totalprice;
    @Transient
    List<String> itemList;
}
