package com.suryansh.visitorentry.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This is MongoDb document class that stores user in a database.
 *  This is mainly used for login, signUp activities.
 */
@Document("User_Document")
@Data
@Builder
public class UserDocument {
    @Id
    private String contact;
    private String username;
    private String password;
    private String schoolRole;
    private boolean isValid;
}
