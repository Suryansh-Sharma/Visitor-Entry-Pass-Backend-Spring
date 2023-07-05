package com.suryansh.visitorentry.repository;

import com.suryansh.visitorentry.entity.VisitDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
/**
 * This is a repository interface that communicated with a database layer
 * This is used for VisitDocument.
 * @author suryansh
 */
public interface VisitRepository extends MongoRepository<VisitDocument,String> {
    /**
     * This method is used to find a visitor on a specific date.
     * This method is also used for finding today's visit.
     *
     * @param date Pass date in a format of "YYYY-MM-DD".
     * @param pageable It accepts a Pageable object.
     * @return It will return a page of visitor document.
     */
    @Query("{'visitingRecords': { $elemMatch: { 'date': ?0 } }}")
    Page<VisitDocument> findVisitBySpecificDate(String date, Pageable pageable);
}
