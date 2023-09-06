package com.abdullahturhan.rabbitmqintegration.service;

import com.abdullahturhan.rabbitmqintegration.dto.UpdateUserDto;
import com.abdullahturhan.rabbitmqintegration.dto.UserDto;
import com.abdullahturhan.rabbitmqintegration.entity.User;
import com.abdullahturhan.rabbitmqintegration.rerpository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOne(Long id){
        User user = userRepository.findById(id).get();
        return  user;
    }
    @Transactional
    @RabbitListener(queues = "transaction_queue")
    public void create(UserDto userDto){
        User user = User.builder()
                .fullName(userDto.getFullName())
                .amount(userDto.getAmount())
                .build();
        userRepository.save(user);
        LOG.info(String.format("User created successfully %S",user.toString()));
    }
    @Transactional
    public void update(Long id, UpdateUserDto userDto){
        Optional<User> userFromDb = userRepository.findById(id);
        userFromDb.ifPresent(model ->{
            model.setFullName(userDto.getFullName());
            userRepository.save(model);
        });

    }
    @Transactional
    public void updateAmount(Long id, Double amount){
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(model ->{
            model.setAmount(amount);
            userRepository.save(model);
        });
    }

    @Transactional
    public void delete(Long id){
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userRepository::delete);
    }
}
