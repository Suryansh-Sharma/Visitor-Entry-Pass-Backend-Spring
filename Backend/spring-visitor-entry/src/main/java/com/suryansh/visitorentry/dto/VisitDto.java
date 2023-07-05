package com.suryansh.visitorentry.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.Instant;
import java.util.List;

/**
 * This class acts as a model and dto for addNewVisit, search, getTodayVisit etc.
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitDto {
    @Pattern(regexp = "^[0-9]{10}$",message = "Contact pattern is wrong")
    private String visitorContact;
    @NotBlank(message = "visitor name can't be blank")
    private String visitorName;
    @NotBlank(message = "Visitor Image can't be blank ")
    private String visitorImage;
    private String reason;
    private String location;

    private String latestVisitDate;
    private String latestVisitTime;

    @Valid
    private VisitorAddressDto visitorAddress;

    private boolean hasChildrenInSchool;

    private List<VisitorChildDto>visitorChildren;

    private List<VisitingRecordDto>visitingRecord;

    @Data
    @ToString
    public static class VisitorAddressDto {
        @NotBlank(message = "city name can't be blank")
        private String city;
        @NotBlank(message = "pin-code can't be blank")
        private String pinCode;
        private String line1;
    }

    @Data
    @ToString
    public static class VisitorChildDto {
        private String childName;
        private String childClass;
    }

    @Data
    @ToString
    public static class VisitingRecordDto{
        private String date;
        private String time;
        private String reason;
        private String location;
        private String status;
        private Instant calledOn;
        private Instant visitCompletedOn;
    }
}
