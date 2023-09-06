package com.abdullahturhan.rabbitmqintegration.controller;

import com.abdullahturhan.rabbitmqintegration.dto.TransactionDto;
import com.abdullahturhan.rabbitmqintegration.service.RabbitMqService;
import com.abdullahturhan.rabbitmqintegration.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final RabbitMqService rabbitMqService;

    public TransactionController(TransactionService transactionService, RabbitMqService rabbitMqService) {
        this.transactionService = transactionService;
        this.rabbitMqService = rabbitMqService;
    }
    @PostMapping("")
    public ResponseEntity<String> transaction(@RequestBody TransactionDto transactionDto){
        transactionService.transfer(transactionDto);
        final String message = "Your money transfer transaction has been successfully received!";
        rabbitMqService.sendMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }
}
