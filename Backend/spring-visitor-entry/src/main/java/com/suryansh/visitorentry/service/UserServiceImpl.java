package com.suryansh.visitorentry.service;

import com.suryansh.visitorentry.dto.UserDto;
import com.suryansh.visitorentry.entity.UserDocument;
import com.suryansh.visitorentry.exception.SpringVisitorException;
import com.suryansh.visitorentry.model.UserModel;
import com.suryansh.visitorentry.repository.UserRepository;
import com.suryansh.visitorentry.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class is used for performing user authentication operation.
 *
 * @author suryansh
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto addNewUser(UserModel model) {
        Optional<UserDocument> checkDocument = userRepository.findById(model.getContact());
        if (checkDocument.isPresent()){
            throw new SpringVisitorException("User contact is already present ", ErrorType.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
        UserDocument userDocument = UserDocument.builder()
                .contact(model.getContact())
                .username(model.getUsername())
                .password(model.getPassword())
                .schoolRole(model.getSchoolRole())
                .isValid(true)
                .build();
        try {
            userRepository.save(userDocument);
            logger.info("New User {} is added successfully :addNewUser ",model.getUsername());
            return new UserDto(
                    model.getUsername(),
                    "",
                    model.getSchoolRole(),
                    model.getContact(),
                    true
            );
        }catch (Exception e){
            logger.error("Unable to add new user :addNewUser "+e);
            throw new SpringVisitorException("Sorry unable to add new user", ErrorType.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public UserDto verifyUser(UserDto dto) {
        UserDocument checkDocument = userRepository.findById(dto.contact())
                .orElseThrow(()->new SpringVisitorException("Invalid Credentials ",ErrorType.NOT_FOUND,HttpStatus.BAD_REQUEST));
        if (!dto.password().equals(checkDocument.getPassword())){
            throw new SpringVisitorException("Invalid Credentials ",ErrorType.NOT_FOUND,HttpStatus.BAD_REQUEST);
        }
        return new UserDto(
                checkDocument.getUsername(),
                "",
                checkDocument.getSchoolRole(),
                checkDocument.getContact(),
                checkDocument.isValid()
        );
    }
}
