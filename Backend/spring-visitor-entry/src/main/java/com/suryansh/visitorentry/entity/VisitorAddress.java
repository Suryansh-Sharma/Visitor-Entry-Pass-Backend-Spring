package com.suryansh.visitorentry.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitorAddress {
    private String city;
    private String pinCode;
    private String line1;
}
