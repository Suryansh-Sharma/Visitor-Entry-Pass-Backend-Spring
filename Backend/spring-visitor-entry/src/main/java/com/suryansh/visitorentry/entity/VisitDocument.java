package com.suryansh.visitorentry.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * This is MongoDb document class that stores visit in a database.
 *  This is mainly used for visit's activities.
 */
@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitDocument {
    @Id
    private String visitorContact;
    private String visitorName;
    private String visitorImage;

    // Visitor Address.
    private VisitorAddress visitorAddress;

    private boolean hasChildrenInSchool;
    private String latestVisitDate;
    private String latestVisitTime;
    private boolean visitCompleted;

    // Children
    private List<VisitorChildren> visitorChildren;

    // Visiting Records
    private List<VisitingRecord>visitingRecords;
}














