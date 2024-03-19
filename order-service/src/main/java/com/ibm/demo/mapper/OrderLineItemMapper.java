package com.ibm.demo.mapper;
import com.ibm.demo.entity.OrderLineItem;
import com.ibm.demo.dto.OrderLineItemDto;

public class OrderLineItemMapper {
    public static OrderLineItemDto mapToOrderLineItemDto(OrderLineItem orderLineItem){
        OrderLineItemDto orderLineItemDto = new OrderLineItemDto(
                orderLineItem.getId(),
                orderLineItem.getItemname(),
                orderLineItem.getItemquantity(),
                orderLineItem.getOrderid()
        );
        return orderLineItemDto;
    }

    public static OrderLineItem mapToOrderLineItem(OrderLineItemDto orderLineItemDto){
        OrderLineItem orderLineItem = new OrderLineItem(
                orderLineItemDto.getId(),
                orderLineItemDto.getItemname(),
                orderLineItemDto.getItemquantity(),
                orderLineItemDto.getOrderid()
        );
        return orderLineItem;
    }
}
