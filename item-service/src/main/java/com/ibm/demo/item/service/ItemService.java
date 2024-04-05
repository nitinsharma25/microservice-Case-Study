package com.ibm.demo.item.service;

import com.ibm.demo.item.dto.ItemDto;
import com.ibm.demo.item.entity.Item;
import com.ibm.demo.item.exception.ItemNotFoundException;
import com.ibm.demo.item.mapper.ItemMapper;
import com.ibm.demo.item.reository.ItemRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {


    @Autowired
    ItemRepository itemRepository;


    public ItemDto createItem(ItemDto itemDto){
        Item mappedItem = ItemMapper.mapToItem(itemDto);
        Item savedItem = itemRepository.save(mappedItem);
       ItemDto savedItemDto = ItemMapper.mapToItemDto(savedItem);
       return savedItemDto;
    }

    public ItemDto getItemById(String id){
       //Item item = itemRepository.findById(id).get();
        Item item  = itemRepository.findById(id).orElseThrow(
                ()-> new ItemNotFoundException("Item","id",id)
        );

        ItemDto savedItemDto = ItemMapper.mapToItemDto(item);
        return savedItemDto;
    }

    public void deleteItemById(String id){
         itemRepository.deleteById(id);
    }
    public ItemDto updateItem(ItemDto itemDto){
       // return itemRepository.save(item);
        Item mappedItem = ItemMapper.mapToItem(itemDto);
        Item savedItem = itemRepository.save(mappedItem);
        ItemDto savedItemDto = ItemMapper.mapToItemDto(savedItem);
        return savedItemDto;
    }

    public List<ItemDto> getAllItem(){

        List<ItemDto> itemDtoList = new ArrayList<ItemDto>();
        List<Item> itemList = itemRepository.findAll();
        for (Item item:itemList){
           ItemDto itemDto = ItemMapper.mapToItemDto(item);
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

   /* public List<ItemDto> findByItemName(String name){
       // return itemRepository.findByName(name);
      //  itemRepository.findByName(name).orE
        List<ItemDto> itemDtoList = new ArrayList<ItemDto>();
        List<Item> itemList = itemRepository.findByName(name);
        for (Item item:itemList){
            ItemDto itemDto = ItemMapper.mapToItemDto(item);
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }*/
   public ItemDto findByItemName(String name){
       // return itemRepository.findByName(name);
       //  itemRepository.findByName(name).orE
       Item item = itemRepository.findByName(name).orElseThrow(
               ()-> new ItemNotFoundException("Item","name",name)
       );
           ItemDto itemDto = ItemMapper.mapToItemDto(item);

       return itemDto;
   }
}
