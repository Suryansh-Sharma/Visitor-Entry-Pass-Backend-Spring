package com.suryansh.visitorentry.entity;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
@Data
@Builder
public class VisitingRecord {
    private String date;
    private String time;
    private String reason;
    private String location;
    private String status;
    private Instant calledOn;
    private Instant visitCompletedOn;
}
