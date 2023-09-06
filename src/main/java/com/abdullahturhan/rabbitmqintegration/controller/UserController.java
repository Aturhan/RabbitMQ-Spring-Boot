package com.abdullahturhan.rabbitmqintegration.controller;

import com.abdullahturhan.rabbitmqintegration.dto.UpdateUserDto;
import com.abdullahturhan.rabbitmqintegration.dto.UserDto;
import com.abdullahturhan.rabbitmqintegration.service.RabbitMqService;
import com.abdullahturhan.rabbitmqintegration.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private final UserService userService;
    private final RabbitMqService rabbitMqService;

    public UserController(UserService userService, RabbitMqService rabbitMqService) {
        this.userService = userService;
        this.rabbitMqService = rabbitMqService;
    }
    @PostMapping(path = "")
    public ResponseEntity<String> createOneUser(@RequestBody UserDto userDto){
         userService.create(userDto);
         final String message = "User created successfully";
         rabbitMqService.sendMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<String> updateOneUser(@PathVariable Long id,@RequestBody UpdateUserDto userDto){
        userService.update(id,userDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteOneUser(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
