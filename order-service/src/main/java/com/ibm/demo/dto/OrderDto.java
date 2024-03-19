package com.ibm.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
        private String id;
        private String orderdate;
        private String orderdesc;
        private String custid;
        private Double totalprice;
        private List<String> itemList;
}
