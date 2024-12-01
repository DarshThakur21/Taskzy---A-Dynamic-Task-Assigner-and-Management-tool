package com.Taskzy.UserTaskService.controller;


import com.Taskzy.UserTaskService.Model.User;
import com.Taskzy.UserTaskService.Service.CustomUserServiceImplmentation;
import com.Taskzy.UserTaskService.repository.UserRepo;
import com.Taskzy.UserTaskService.request.LoginRequest;
import com.Taskzy.UserTaskService.response.AuthResponse;
import com.Taskzy.UserTaskService.securityConfig.JwtProvider;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CustomUserServiceImplmentation customUserServiceImplmentation;

    @Autowired
    private PasswordEncoder passwordEncoder;



//sigup api calls
//    createUserHandler
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> userSignupHandler(@RequestBody User user) throws Exception{
        String email=user.getEmail();
        String password= user.getPassword();
        String fullName= user.getFullName();
        String role= user.getRole();

        User isEmailExist=userRepo.findByEmail(email);
        if(isEmailExist!=null){
                throw new Exception("email already exist with another account ");
        }

//        joining or signing up the new user
        User newUser=new User();
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setRole(role);
        newUser.setPassword(passwordEncoder.encode(password)    );

        User savedUser=userRepo.save(newUser);

//        authenticating the user and generating tokens
        Authentication authentication=new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token= JwtProvider.generateToken(authentication);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("successful");
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }


//    signin methods api call

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> sigin(@RequestBody LoginRequest loginRequest) throws  Exception{
            String username= loginRequest.getEmail();
            String password= loginRequest.getPassword();

        System.out.println(username +"_________"+password);

        Authentication authentication=authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=JwtProvider.generateToken(authentication);
        AuthResponse authResponse=new AuthResponse();

        authResponse.setMessage("login successfull");
        authResponse.setJwt(token);
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails= customUserServiceImplmentation.loadUserByUsername(username);

        System.out.println("singin credentials: "+userDetails);
        if (userDetails==null){
            System.out.println("singin in userdetails -null "+userDetails);
            throw  new BadCredentialsException("username or password not found ");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            System.out.println("username password does not matches "+userDetails);
            throw new BadCredentialsException("username or password not found");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }


}
