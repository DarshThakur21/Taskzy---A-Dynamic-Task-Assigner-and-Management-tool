package com.Taskzy.UserTaskService.Service;

import com.Taskzy.UserTaskService.Model.User;
import com.Taskzy.UserTaskService.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserServiceImplmentation implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;


//    this is user to  custom set username and password and to avoid auto generated of passwords
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User  user=userRepo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("user not found with email : "+username);
        }
        List<GrantedAuthority> authorities=new ArrayList<>();


        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
    }
}
