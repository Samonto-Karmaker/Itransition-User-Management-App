package com.example.usermanagement.services;

import com.example.usermanagement.models.User;
import com.example.usermanagement.repositories.UserRepository;
import com.example.usermanagement.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    MyUserDetailsService myUserDetailsService;
    JwtUtil jwtUtil;
    PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            MyUserDetailsService myUserDetailsService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return jwtUtil.generateToken(user.getEmail());
    }

    public String login(String email, String password) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception e) {
            throw new Exception("Invalid username or password");
        }
        User user = userRepository.findByEmail(email);
        if (user.isBlocked()) throw new Exception("User is blocked");
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        return jwtUtil.generateToken(user.getEmail());
    }

    public String logout() {
        return "Logged out successfully";
    }

}
