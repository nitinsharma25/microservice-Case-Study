package com.ibm.demo.item.mapper;

import com.ibm.demo.item.dto.ItemDto;
import com.ibm.demo.item.entity.Item;

public class ItemMapper {
    public static ItemDto mapToItemDto(Item item){
        ItemDto itemDto = new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getImage()
                 );
        return itemDto;
    }

    public static Item mapToItem(ItemDto itemDto){
        Item item = new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getPrice(),
                itemDto.getImage()
        );
        return item;
    }
}
