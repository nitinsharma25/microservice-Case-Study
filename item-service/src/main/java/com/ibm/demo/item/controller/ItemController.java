package com.ibm.demo.item.controller;

import com.ibm.demo.item.dto.ItemDto;
import com.ibm.demo.item.entity.Item;
import com.ibm.demo.item.exception.ErrorDetails;
import com.ibm.demo.item.exception.ItemNotFoundException;
import com.ibm.demo.item.reository.ItemRepository;
import com.ibm.demo.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @PostMapping("/create")
    public ItemDto createItem(@RequestBody ItemDto itemDto){
        return itemService.createItem(itemDto);
    }

    @GetMapping("/getItemById/{id}")
    public ItemDto getItemById(@PathVariable String id){
        return itemService.getItemById(id);
    }

    @GetMapping("/items")
    public List<ItemDto> getAllItem(){
        return itemService.getAllItem();
    }
    @PutMapping("/updateItemById")
    public ItemDto updateItemById(@RequestBody ItemDto itemDto){
        return itemService.updateItem(itemDto);
    }

    @DeleteMapping("/deleteItemById/{id}")
    public String deleteItemById(@PathVariable String id){
         itemService.deleteItemById(id);
        System.out.println("Delete Success for id "+id);
         return "delete Success";
    }
    /*@GetMapping("/items/{name}")
    public List<ItemDto> findItemById(@PathVariable String name){
      return   itemService.findByItemName(name);
      //  return "delete Success";
    }*/
    @GetMapping("/items/{name}")
    public ItemDto findItemById(@PathVariable String name){
        return   itemService.findByItemName(name);
        //  return "delete Success";
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleItemNotFoundException(ItemNotFoundException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "Item Not Found"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
