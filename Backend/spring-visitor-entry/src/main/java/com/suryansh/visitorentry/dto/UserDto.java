package com.suryansh.visitorentry.dto;

/**
 * This java record class is used for user operations.
 * Currently, It acts as DTO for user login,sign-up.
 *
 * @param username It includes info about username
 * @param password It includes password.
 * @param schoolRole It includes info about a role e.g.: Teacher, Receptionist etc
 * @param contact It contains contact number of user.
 * @param isValid It tells a whether a user account is valid or not.
 */
public record UserDto(String username,String password,String schoolRole,String contact,boolean isValid) {
}
