package com.ibm.demo.mapper;

import com.ibm.demo.entity.Order;
import com.ibm.demo.dto.OrderDto;

public class OrderMapper {
    public static OrderDto mapToOrderDto(Order order){
        OrderDto orderDto = new OrderDto(
                order.getId(),
                order.getOrderdate(),
                order.getOrderdesc(),
                order.getCustid(),
                order.getTotalprice(),
                order.getItemList()
        );
        return orderDto;
    }

    public static Order mapToOrder(OrderDto orderDto){
        Order order = new Order(
                orderDto.getId(),
                orderDto.getOrderdate(),
                orderDto.getOrderdesc(),
                orderDto.getCustid(),
                orderDto.getTotalprice(),
                orderDto.getItemList()
        );
        return order;
    }
}
