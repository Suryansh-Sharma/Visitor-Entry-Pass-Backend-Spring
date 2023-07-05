package com.suryansh.visitorentry.service.interfaces;

import com.suryansh.visitorentry.dto.PagingVisitDto;
import com.suryansh.visitorentry.dto.VisitDto;

import java.util.concurrent.CompletableFuture;

public interface VisitorService {
    CompletableFuture<VisitDto> addNewVisitInDb(VisitDto visitModel);

    VisitDto getVisitorDetailByContact(String visitorContact);

    PagingVisitDto getVisitorOnSpecificDate(String date, int pageSize, int pageNumber);

    PagingVisitDto getVisitorOfToday(int pageSize, int pageNumber);

    PagingVisitDto searchRecords(String searchData, String date, String field, int pageSize, int pageNumber);
}
