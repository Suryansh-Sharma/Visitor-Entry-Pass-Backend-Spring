package com.suryansh.visitorentry.controller;

import com.suryansh.visitorentry.dto.PagingVisitDto;
import com.suryansh.visitorentry.dto.VisitDto;
import com.suryansh.visitorentry.service.interfaces.VisitorService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CompletableFuture;

/**
* This Controller class is responsible for handling visitor-related operation
* Handles Http GraphQL request.
* @author suryansh
*/
@Controller
public class VisitController {
    private final VisitorService visitorService;

    public VisitController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    /**
     * Add a new visit to the database.
     * @param visitModel The visit data to be added.
     * @return A completableFuture representing the asynchronous result of adding a visit dto {@link VisitDto}.
     */
    @MutationMapping
    public CompletableFuture<VisitDto> addNewVisit(@Argument("input") @Valid VisitDto visitModel){
        return visitorService.addNewVisitInDb(visitModel);
    }

    /**
     * Get Visitor details by its contact no.
     * @param visitorContact The visitor contact number.
     * @return The visitor details dto {@link VisitDto}.
     */
    @QueryMapping
    public VisitDto getVisitorByContact(@Argument String visitorContact){
        return visitorService.getVisitorDetailByContact(visitorContact);
    }

    /**
     * This api is just for test, It retrieves all visitors on a specific date.
     * @param date The date in format "yyyy-MM-dd".
     * @param pageSize pass page-size for pagination.
     * @param pageNumber pass page-number.
     * @return A PagingVisitDto {@link PagingVisitDto} object containing the page size, page number, list of visits, total pages, and total data.
     */
    @QueryMapping
    public PagingVisitDto getVisitorOnSpecificDate(@Argument String date,@Argument int pageSize
            ,@Argument int pageNumber){
        return visitorService.getVisitorOnSpecificDate(date,pageSize,pageNumber);
    }

    /**
     * This api is used to get today's all visits.
     * @param pageSize Pass page size for pagination.
     * @param pageNumber Pass page-no for pagination.
     * @return A PagingVisitDto {@link PagingVisitDto} object containing the page size, page number, list of visits, total pages, and total data.
     */
    @QueryMapping
    public PagingVisitDto getTodayAllVisit(@Argument int pageSize
            ,@Argument int pageNumber){
        return visitorService.getVisitorOfToday(pageSize,pageNumber);
    }

    /**
     * Searches for visitors based on their contact, name, location, date, child, etc.
     *
     * @param searchData The search data value to be searched.
     * @param date The date in the format "yyyy-MM-dd".
     * @param field The field on which the visitor is going to be searched (e.g., visitorName, contact, location).
     * @param pageSize The page size for pagination.
     * @param pageNumber The page number for pagination.
     * @return A PagingVisitDto {@link PagingVisitDto} object containing the page size, page number, list of visits, total pages, and total data.
     */
    @QueryMapping
    public PagingVisitDto searchVisitor(@Argument String searchData,@Argument String date,
                                        @Argument String field,@Argument int pageSize
            ,@Argument int pageNumber){
        return visitorService.searchRecords(searchData,date,field,pageSize,pageNumber);
    }

}
