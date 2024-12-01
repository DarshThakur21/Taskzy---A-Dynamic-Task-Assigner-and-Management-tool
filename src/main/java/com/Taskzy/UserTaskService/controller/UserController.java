package com.Taskzy.UserTaskService.controller;


import com.Taskzy.UserTaskService.Model.User;
import com.Taskzy.UserTaskService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/user")
@RestController
public class UserController {


    @Autowired
    private UserService userService;



    @GetMapping("/profile")
    public ResponseEntity<User> geUserProfile(@RequestHeader("Authorization") String jwt){
        User user=userService.getUserProfile(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String jwt){
        List<User> users=userService.getAllUsers();
        return  new ResponseEntity<>(users,HttpStatus.OK);
    }
}
