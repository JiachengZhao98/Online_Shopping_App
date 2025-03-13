package com.superdupermart.shoppingapp.service.auth;

import com.superdupermart.shoppingapp.dao.UserDao;
import com.superdupermart.shoppingapp.dto.auth.UserRegistrationDto;
import com.superdupermart.shoppingapp.dto.auth.LoginDto;
import com.superdupermart.shoppingapp.entity.User;
import com.superdupermart.shoppingapp.exception.InvalidCredentialsException;
import com.superdupermart.shoppingapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.superdupermart.shoppingapp.security.UserDetailsServiceImpl;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    @Transactional
    public void register(UserRegistrationDto registrationDto) {
        if(userDao.findByUsername(registrationDto.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if(userDao.findByEmail(registrationDto.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        if (registrationDto.getUsername().startsWith("admin")) {
            user.setRole("ROLE_SELLER");
        } else {
            user.setRole("ROLE_BUYER");
        }
        userDao.save(user);
    }

    @Override
    public String login(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Incorrect credentials, please try again.");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
        return jwtUtil.generateToken(userDetails);
    }
}
