package com.suryansh.visitorentry.repository;

import com.suryansh.visitorentry.entity.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * This is a repository interface that communicated with a database layer
 * This is used for UserDocument.
 * @author suryansh
 */
@Repository
public interface UserRepository extends MongoRepository<UserDocument,String> {
}
