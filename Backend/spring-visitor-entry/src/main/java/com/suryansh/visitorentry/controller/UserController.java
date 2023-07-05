package com.suryansh.visitorentry.controller;

import com.suryansh.visitorentry.dto.UserDto;
import com.suryansh.visitorentry.model.UserModel;
import com.suryansh.visitorentry.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

/**
 * This Controller class is used for register new user, login user.
 * This class in under development, so new features like JWT, Spring-Security etc. will be added soon.
 */
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @MutationMapping
    public UserDto registerNewUser(@Argument("input") @Valid UserModel model){
        return userService.addNewUser(model);
    }
    @MutationMapping
    public UserDto loginUser(@Argument("input") UserDto userDto){
        return userService.verifyUser(userDto);
    }
}
