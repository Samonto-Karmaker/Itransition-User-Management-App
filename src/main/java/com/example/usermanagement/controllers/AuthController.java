package com.example.usermanagement.controllers;

import com.example.usermanagement.models.AuthRequest;
import com.example.usermanagement.models.AuthResponse;
import com.example.usermanagement.models.User;
import com.example.usermanagement.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String redirect() {
        return "redirect:/login";
    }

    @GetMapping("register")
    public String getRegisterPage() {
        return "register";
    }

    @GetMapping("login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(new AuthResponse(authService.register(user)));
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) throws Exception {
        return ResponseEntity.ok(
                new AuthResponse(
                        authService.login(authRequest.getEmail(), authRequest.getPassword())
                )
        );

    }

    @DeleteMapping("logout")
    public ResponseEntity<?> logout() {
       return ResponseEntity.ok(authService.logout());
    }
}
