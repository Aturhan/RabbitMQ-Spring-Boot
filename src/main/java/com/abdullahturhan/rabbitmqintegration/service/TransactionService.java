package com.abdullahturhan.rabbitmqintegration.service;

import com.abdullahturhan.rabbitmqintegration.dto.TransactionDto;
import com.abdullahturhan.rabbitmqintegration.entity.Transaction;
import com.abdullahturhan.rabbitmqintegration.entity.User;
import com.abdullahturhan.rabbitmqintegration.rerpository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TopicExchange topicExchange;
    private final AmqpTemplate amqpTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);
    @Value("${rabbitmq.routing.json.key}")
    public String key;
    @Value("${rabbitmq.queue.json.name}")
    public String queue;


    public TransactionService(TransactionRepository transactionRepository, UserService userService, TopicExchange topicExchange, AmqpTemplate amqpTemplate) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.topicExchange = topicExchange;
        this.amqpTemplate = amqpTemplate;
    }
    @Transactional
    @RabbitListener(queues = "transaction_queue")
    public void transfer(TransactionDto transactionDto){
        User user =  userService.findOne(transactionDto.getUserId());

        if(transactionDto.getTransferredAmount() > user.getAmount()){
            throw new RuntimeException("Not enough amount for  transaction to transfer ");
        }
       final  Transaction transaction = Transaction.builder()
                .transferTo(transactionDto.getTransferTo())
                .transferredAmount(transactionDto.getTransferredAmount())
                .user(user)
                .build();

        transactionRepository.save(transaction);

        LOG.info(String.format("Transaction transfer to %s and transferred amount %s ",transactionDto.getTransferTo(),transactionDto.getTransferredAmount()));

        final Double newAmount = user.getAmount() - transactionDto.getTransferredAmount();

        userService.updateAmount(transactionDto.getUserId(),newAmount);


    }
}
