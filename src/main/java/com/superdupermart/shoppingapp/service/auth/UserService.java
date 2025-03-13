package com.superdupermart.shoppingapp.service.auth;

import com.superdupermart.shoppingapp.dto.auth.UserRegistrationDto;
import com.superdupermart.shoppingapp.dto.auth.LoginDto;

public interface UserService {
    void register(UserRegistrationDto registrationDto);
    String login(LoginDto loginDto);
}
