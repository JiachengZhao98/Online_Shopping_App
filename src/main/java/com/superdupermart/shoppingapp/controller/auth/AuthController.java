package com.superdupermart.shoppingapp.controller.auth;

import com.superdupermart.shoppingapp.dto.auth.AuthResponse;
import com.superdupermart.shoppingapp.dto.auth.LoginDto;
import com.superdupermart.shoppingapp.dto.auth.UserRegistrationDto;
import com.superdupermart.shoppingapp.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Validated @RequestBody UserRegistrationDto registrationDto) {
        userService.register(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
