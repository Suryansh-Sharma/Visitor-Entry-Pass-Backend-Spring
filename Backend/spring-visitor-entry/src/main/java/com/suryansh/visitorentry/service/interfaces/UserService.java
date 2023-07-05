package com.suryansh.visitorentry.service.interfaces;

import com.suryansh.visitorentry.dto.UserDto;
import com.suryansh.visitorentry.model.UserModel;

public interface UserService {
    UserDto addNewUser(UserModel model);

    UserDto verifyUser(UserDto userDto);
}
