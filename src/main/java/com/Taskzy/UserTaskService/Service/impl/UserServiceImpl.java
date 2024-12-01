package com.Taskzy.UserTaskService.Service.impl;

import com.Taskzy.UserTaskService.Model.User;
import com.Taskzy.UserTaskService.Service.UserService;
import com.Taskzy.UserTaskService.repository.UserRepo;
import com.Taskzy.UserTaskService.securityConfig.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;



    @Override
    public User getUserProfile(String jwt) {
      String email=  JwtProvider.getEmailFromJwtToken(jwt);
      return  userRepo.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
